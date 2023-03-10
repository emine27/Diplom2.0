package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.pages.MainPage;
import ru.netology.pages.PayPage;

import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {
    MainPage mainPage = open("http://localhost:8080/", MainPage.class);

    @BeforeEach
    void setUP() {
        Configuration.holdBrowserOpen = true;
    }

    @AfterEach
    void tearDown() {
        closeWindow();
    }


    @Test
    void shouldSuccessTransactionWithPaymentCard() {
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithApprovedCard();
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getPaymentCardData().getStatus());
    }

    @Test
    void shouldDeclineTransactionWithPaymentCard(){
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithDeclinedCard();
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.errorRejectedFromBank();
        assertEquals("DECLINE", SQLHelper.getPaymentCardData().getStatus());
    }

    @Test
    void shouldDeclineTransactionWithRandomNumberCard() {
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithRandomCard();
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.errorRejectedFromBank();
        assertEquals("DECLINE", SQLHelper.getPaymentCardData().getStatus());
    }

    @Test
    void shouldShowAttentionTextEmptyCardNumberField() {
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithApprovedCard();
        var payPage = new PayPage();
        payPage.insertPayCardWithEmptyCardNumber(cardInfo);
        payPage.attentionUnderNumberCard("???????????????? ????????????");

    }

    @Test
    void shouldShowAttentionTextEmptyMonthField() {
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithApprovedCard();
        var payPage = new PayPage();
        payPage.insertPayCardWithEmptyMonth(cardInfo);
        payPage.attentionUnderMonth("???????????????? ????????????");
    }

    @Test
    void shouldShowAttentionTextEmptyYearField() {
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithApprovedCard();
        var payPage = new PayPage();
        payPage.insertPayCardEmptyYear(cardInfo);
        payPage.attentionUnderYear("???????????????? ????????????");
    }

    @Test
    void shouldShowAttentionTextEmptyOwnerField() {
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithApprovedCard();
        var payPage = new PayPage();
        payPage.insertPayCardEmptyOwner(cardInfo);
        payPage.attentionUnderOwner("???????? ?????????????????????? ?????? ????????????????????");
    }

    @Test
    void shouldShowAttentionTextEmptyCVCField() {
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithApprovedCard();
        var payPage = new PayPage();
        payPage.insertPayCardEmptyCVC(cardInfo);
        payPage.attentionUnderCVC("???????????????? ????????????");
    }

    @Test
    void shouldDeclineTransactionWithDubleZeroMonth() {
        mainPage.payPage();
        var validYear = Integer.parseInt(DataHelper.getCurrentYear()) + 1;
        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear("00", String.valueOf(validYear));
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.errorRejectedFromBank();
    }

    @Test
    void shouldShowAttentionTextOneFigureMonth() {
        mainPage.payPage();
        var validYear = Integer.parseInt(DataHelper.getCurrentYear()) + 1;
        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear("1", String.valueOf(validYear));
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.attentionUnderMonth("???????????????? ????????????");

    }

    @Test
    void shouldSuccessTransactionWithMaxAllowedDate() {
        mainPage.payPage();
        var currentMonth = DataHelper.getCurrentMonth();
        var maxYear = Integer.parseInt(DataHelper.getCurrentYear()) + 5;
        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear(currentMonth, String.valueOf(maxYear));
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getPaymentCardData().getStatus());
    }

    @Test
    void shouldSuccessTransactionWithMinAllowedDate() {
        mainPage.payPage();
        var currentMonth = DataHelper.getCurrentMonth();
        var currentYear = DataHelper.getCurrentYear();
        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear(currentMonth, currentYear);
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getPaymentCardData().getStatus());
    }

    @Test
    void shouldDeclineTransactionWithPastMonth() {
        mainPage.payPage();
        var currentMonth = Integer.parseInt(DataHelper.getCurrentMonth());
        var pastMonth = 0;
        var currentYearMinusMonth = Integer.parseInt(DataHelper.getCurrentYear());
        if (currentMonth == 1) {
            pastMonth = 12;
            currentYearMinusMonth = currentYearMinusMonth - 1;
        } else pastMonth = currentMonth - 1;
        String pastMonthZero = "";
        if (pastMonth < 10) {
            pastMonthZero = "0" + pastMonth;
        }

        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear
                (String.valueOf(pastMonthZero), String.valueOf(currentYearMinusMonth));
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.attentionUnderMonth("?????????????? ???????????? ???????? ???????????????? ??????????");
    }

    @Test
    void shouldDeclineTransactionWithPastYear() {
        mainPage.payPage();
        var currentMonth = Integer.parseInt(DataHelper.getCurrentMonth());
        var pastYear = Integer.parseInt(DataHelper.getCurrentYear()) - 1;
        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear
                (String.valueOf(currentMonth), String.valueOf(pastYear));
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.attentionUnderYear("?????????? ???????? ???????????????? ??????????");
    }

    @Test
    void shouldDeclineTransactionWithInvalidMonth(){
        mainPage.payPage();
        var currentYear = DataHelper.getCurrentYear();
        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear
                (String.valueOf("50"), currentYear);
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.attentionUnderMonth("?????????????? ???????????? ???????? ???????????????? ??????????");

    }

    @Test
    void shouldSuccessTransactionWithMaxAllowedDateMinusMonth() {
        mainPage.payPage();
        var currentMonth = Integer.parseInt(DataHelper.getCurrentMonth());
        var pastMonth = 0;
        var maxYear = Integer.parseInt(DataHelper.getCurrentYear()) + 5;
        if (currentMonth == 1) {
            pastMonth = 12;
            maxYear = maxYear - 1;
        } else pastMonth = currentMonth - 1;
        String pastMonthZero = "";
        if (pastMonth < 10) {
            pastMonthZero = "0" + pastMonth;
        }

        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear
                (String.valueOf(pastMonthZero), String.valueOf(maxYear));
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.successFromBank();
    }
    @Test
    void shouldSuccessTransactionWithMaxLengthNameOwner(){
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedLengthOwner(30);
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getPaymentCardData().getStatus());
    }

    @Test
    void shouldSuccessTransactionWithMinLengthNameOwner(){
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedLengthOwner(3);
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getPaymentCardData().getStatus());
    }

    @Test
    void shouldDeclineTransactionWithNoValidMinLengthNameOwner(){
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedLengthOwner(2);
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.attentionUnderOwner("?????????????????? ?????????????? ?????? ?? ?????????????????? ??????????");
    }

    @Test
    void shouldDeclineTransactionWithNoValidMaxLengthNameOwner (){
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedLengthOwner(31);
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.attentionUnderOwner("?????????????????? ?????????????? ?????? ?? ?????????????????? ??????????");
    }

    @Test
    void shouldDeclineTransactionWithNameOwnerOnCyrillic (){
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedOwnerName("???????? ????????????");
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.attentionUnderOwner("?????????????????? ?????????????? ?????? ?? ?????????????????? ??????????");
    }

    @Test
    void shouldApprovedTransactionWithNameOwnerWithDash (){
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedOwnerName("Bob-Jon");
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getPaymentCardData().getStatus());
    }

    @Test
    void shouldDeclineTransactionWithNameOwnerWithNumbers (){
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedOwnerName("Ivan Ivan567ov");
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.attentionUnderOwner("?????????????????? ?????????????? ?????? ?? ?????????????????? ??????????");
    }

    @Test
    void shouldDeclineTransactionWithNameOwnerWithSpecialCharacters (){
        mainPage.payPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedOwnerName("Ivan ????@$%");
        var payPage = new PayPage();
        payPage.insertPayCardData(cardInfo);
        payPage.attentionUnderOwner("?????????????????? ?????????????? ?????? ?? ?????????????????? ??????????");
    }


}
