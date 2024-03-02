package tech.rent.be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.rent.be.dto.BookingRequestDTO;
import tech.rent.be.dto.RealEstateDTO;
import tech.rent.be.entity.Booking;
import tech.rent.be.entity.RealEstate;
import tech.rent.be.entity.Users;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    RealEstateRepository realEstateRepository;

    @Autowired
    AccountUtils accountUtils;

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

    public String getVnPay(BookingRequestDTO bookingRequestDTO) throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);
        Booking booking = new Booking();
        RealEstate realEstate = realEstateRepository.findRealEstateById(bookingRequestDTO.getId());
        booking.setBookingDate(bookingRequestDTO.getDate());
        booking.setRealEstate(realEstate);
        booking.setUsers(accountUtils.getCurrentUser());
        booking.setStatus(false);
        Booking newBooking = bookingRepository.save(booking);

        String tmnCode = "40A49IOQ";
        String secretKey = "AEEWMWRNYYHFFBUSVJDVOLNYMRXTBMTQ";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://localhost:8081/success";

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
        vnpParams.put("vnp_Amount", String.valueOf(4 * 10000000));
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
