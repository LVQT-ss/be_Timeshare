package tech.rent.be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.rent.be.dto.*;
import tech.rent.be.entity.*;
import tech.rent.be.repository.BookingRepository;
import tech.rent.be.repository.PaymentRepository;
import tech.rent.be.repository.RealEstateRepository;
import tech.rent.be.utils.AccountUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RealEstateRepository realEstateRepository;

    @Autowired
    AccountUtils accountUtils;

    public Payment createPayment(PaymentDTO paymentDTO){
        Booking booking = bookingRepository.findBookingById(paymentDTO.getBookingId());
        Payment payment = new Payment();
        payment.setName(paymentDTO.getEstateName());
        payment.setPrice(paymentDTO.getPrice());

        payment.setBooking(booking);

        return paymentRepository.save(payment);
    }

    public List<BookingResponse> getAllBooking() {
        List<BookingResponse> bookingResponseList = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findBookingsByUsers(accountUtils.getCurrentUser());
        for(Booking b: bookings){
            BookingResponse bookingResponse = new BookingResponse();
            bookingResponse.setId(b.getId());
            bookingResponse.setRealEstate(b.getRealEstate());
            bookingResponse.setBookingDate(b.getBookingDate());

            bookingResponse.setAmount(b.getAmount());
            bookingResponse.setCheckIn(b.getCheckIn());
            bookingResponse.setCheckOut(b.getCheckOut());
            bookingResponse.setPrice(b.getPrice());
            bookingResponse.setStatus(b.getStatus());
//
//            bookingResponse.setBookingStatus(b.getBookingStatus());
//            bookingResponse.setUsers(b.getUsers());
            bookingResponseList.add(bookingResponse);

        }
        return bookingResponseList;
    }

    public List<Booking> getAllBookingOfMember() {
        List<Booking> bookings = new ArrayList<>();
        List<RealEstate> realEstates = realEstateRepository.findRealEstatesByUsers(accountUtils.getCurrentUser());
        for(RealEstate realEstate: realEstates){
            List<Booking> bookings1 = bookingRepository.findBookingsByRealEstate(realEstate);
            bookings.addAll(bookings1);
        }
        return bookings;
    }
}
