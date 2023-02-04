package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));
    private static String numberApprovedCard = "4444 4444 4444 4441";
    private static String numberDeclinedCard = "4444 4444 4444 4442";
    private static int validYear = Integer.parseInt(getCurrentYear()) + 1;

    public static String getCurrentYear() {
        LocalDate date = LocalDate.now();
        String currentYear = date.format(DateTimeFormatter.ofPattern("yy"));
        return currentYear;
    }

    public static String getCurrentMonth() {
        LocalDate date = LocalDate.now();
        String currentMonth = date.format(DateTimeFormatter.ofPattern("MM"));
        return currentMonth;
    }

    public static CardInfo generatedDataWithApprovedCard() {
        var randomName = faker.name().fullName();
        var randomCvc = faker.number().digits(3);
        return new CardInfo(numberApprovedCard, getCurrentMonth(), getCurrentYear(), randomName, randomCvc);
    }

    public static CardInfo generatedDataWithDeclinedCard() {
        var randomName = faker.name().fullName();
        var randomCvc = faker.number().digits(3);
        return new CardInfo(numberDeclinedCard, getCurrentMonth(), getCurrentYear(), randomName, randomCvc);
    }

    public static CardInfo generatedDataWithRandomCard() {
        var randomCardNumber = faker.number().digits(16);
        var randomName = faker.name().fullName();
        var randomCvc = faker.number().digits(3);
        return new CardInfo(randomCardNumber, getCurrentMonth(), getCurrentYear(), randomName, randomCvc);
    }

    public static CardInfo approvedCardWithParametrizedMonthAndYear(String month, String year) {
        var randomName = faker.name().fullName();
        var randomCVC = faker.number().digits(3);
        return new CardInfo(numberApprovedCard, month, year, randomName, randomCVC);
    }

    public static CardInfo generatedDataWithParametrizedLengthOwner(int length) {
        var randomName = faker.lorem().fixedString(length);
        var randomCVC = faker.number().digits(3);
        return new CardInfo(numberApprovedCard, getCurrentMonth(), String.valueOf(validYear), randomName, randomCVC);
    }

    public static CardInfo generatedDataWithParametrizedOwnerName(String name) {
        var randomCVC = faker.number().digits(3);
        return new CardInfo(numberApprovedCard, getCurrentMonth(), String.valueOf(validYear), name, randomCVC);
    }


    @Value
    public static class CardInfo {
        String cardNumber;
        String month;
        String year;
        String cardOwner;
        String cvc;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditCardData {
        private String id;
        private String bank_id;
        private String created;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentCardData {
        private String id;
        private String amount;
        private String created;
        private String status;
        private String transaction_id;
    }
}
