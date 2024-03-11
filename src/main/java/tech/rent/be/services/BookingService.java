package tech.rent.be.services;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.rent.be.dto.BookingRequestDTO;
import tech.rent.be.dto.RealEstateDTO;
import tech.rent.be.entity.Booking;
import tech.rent.be.entity.RealEstate;
import tech.rent.be.entity.Users;
import tech.rent.be.exception.BadRequest;
import tech.rent.be.repository.BookingRepository;
import tech.rent.be.repository.PaymentRepository;
import tech.rent.be.repository.RealEstateRepository;
import tech.rent.be.repository.UsersRepository;
import tech.rent.be.utils.AccountUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    RealEstateRepository realEstateRepository;

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    RealEstateService realEstateService;




    @Autowired
    UsersRepository usersRepository;
//    public Booking createBooking(BookingRequestDTO BookingRequestDTO){
//        Users users = usersRepository.findUsersById(BookingRequestDTO.getUserId());
//        RealEstate realEstate = realEstateRepository.findRealEstateById(BookingRequestDTO.getEstateId());
//        Booking Booking = new Booking();
//        Booking.setBookingDate(BookingRequestDTO.getBookingDate());
//        Booking.setCheckIn(BookingRequestDTO.getCheckIn());
//        Booking.setCheckOut(BookingRequestDTO.getCheckOut());
//        Booking.setBookingDate(BookingRequestDTO.getBookingDate());
//        Booking.setStatus(BookingRequestDTO.getStatus());
//        Booking.setUsers(users);
//        Booking.setRealEstate(realEstate);
//        return bookingRepository.save(Booking);
//    }

    public static Date convertDate(Date date, int hour, int minute, int second) {
        // Create a Calendar instance and set it to the input date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Set the fixed time
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        // Clear milliseconds to ensure consistent results
        calendar.set(Calendar.MILLISECOND, 0);

        // Get the updated Date from the Calendar
        return calendar.getTime();
    }


    public String getVnPay(BookingRequestDTO bookingRequestDTO) throws Exception{
        String amount = String.valueOf(bookingRequestDTO.getAmount());
        String price = String.valueOf(bookingRequestDTO.getPrice() * 100);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);
        Booking booking = new Booking();
        long realEstateId = bookingRequestDTO.getEstateId();
        RealEstate realEstate = realEstateService.finRealEstateById(realEstateId);
        booking.setBookingDate(bookingRequestDTO.getDate());
        booking.setPrice(bookingRequestDTO.getPrice());
        booking.setAmount(bookingRequestDTO.getAmount());
        booking.setCheckIn(convertDate(bookingRequestDTO.getDate(), 14,0,0));
        Date checkOut = convertDate(bookingRequestDTO.getDate(), 12,0,0);
        Calendar c = Calendar.getInstance();
        c.setTime(checkOut);
        c.add(Calendar.DATE, bookingRequestDTO.getNumberOfDate());
        checkOut = c.getTime();
        booking.setCheckOut(checkOut);

        List<Booking> bookings = bookingRepository.findBookingsByRealEstate(realEstate);
        for (Booking booking1 : bookings){
            if(realEstateService.checkIfBookingFromTo(booking1, booking.getCheckIn(), booking.getCheckOut())){
                throw new BadRequest("Real Estate not available!");
            }
        }


        booking.setRealEstate(realEstate);
        booking.setUsers(accountUtils.getCurrentUser());
        booking.setStatus(false);
        booking.setAmount(bookingRequestDTO.getAmount());
        Booking newBooking = bookingRepository.save(booking);

        String tmnCode = "40A49IOQ";
        String secretKey = "AEEWMWRNYYHFFBUSVJDVOLNYMRXTBMTQ";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://4rent.tech/success";

        String currCode = "VND";
        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", newBooking.getId().toString());
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + newBooking.getId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount",price );
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "128.199.178.23");

        StringBuilder signDataBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("=");
            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("&");
        }
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1); // Remove last '&'

        String signData = signDataBuilder.toString();
        String signed = generateHMAC(secretKey, signData);

        vnpParams.put("vnp_SecureHash", signed);

        StringBuilder urlBuilder = new StringBuilder(vnpUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1); // Remove last '&'

        return urlBuilder.toString();
    }

    public Booking updatePayment(long bookingId){
        Booking booking = bookingRepository.findBookingById(bookingId);
        booking.setStatus(true);
        return bookingRepository.save(booking);
    }
    private String generateHMAC(String secretKey, String signData) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmacSha512.init(keySpec);
        byte[] hmacBytes = hmacSha512.doFinal(signData.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}
