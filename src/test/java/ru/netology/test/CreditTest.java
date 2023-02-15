package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.pages.CreditPage;
import ru.netology.pages.MainPage;
import ru.netology.pages.PayPage;

import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {
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
    void shouldSuccessTransactionWithCreditCard() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithApprovedCard();
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getCreditCardData().getStatus());
    }

    @Test
    void shouldDeclineTransactionWithCreditCard() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithDeclinedCard();
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.errorRejectedFromBank();
        assertEquals("DECLINED", SQLHelper.getCreditCardData().getStatus());
    }

    @Test
    void shouldDeclineTransactionWithRandomNumberCard() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithRandomCard();
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.errorRejectedFromBank();
        assertEquals("DECLINED", SQLHelper.getCreditCardData().getStatus());
    }

    @Test
    void shouldShowAttentionTextEmptyCardNumberField() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithApprovedCard();
        var creditPage = new CreditPage();
        creditPage.insertPayCardWithEmptyCardNumber(cardInfo);
        creditPage.attentionUnderNumberCard("Неверный формат");

    }

    @Test
    void shouldShowAttentionTextEmptyMonthField() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithApprovedCard();
        var creditPage = new CreditPage();
        creditPage.insertPayCardWithEmptyMonth(cardInfo);
        creditPage.attentionUnderMonth("Неверный формат");
    }

    @Test
    void shouldShowAttentionTextEmptyYearField() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithApprovedCard();
        var creditPage = new CreditPage();
        creditPage.insertPayCardEmptyYear(cardInfo);
        creditPage.attentionUnderYear("Неверный формат");
    }

    @Test
    void shouldShowAttentionTextEmptyOwnerField() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithApprovedCard();
        var creditPage = new CreditPage();
        creditPage.insertPayCardEmptyOwner(cardInfo);
        creditPage.attentionUnderOwner("Поле обязательно для заполнения");
    }

    @Test
    void shouldShowAttentionTextEmptyCVCField() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithApprovedCard();
        var creditPage = new CreditPage();
        creditPage.insertPayCardEmptyCVC(cardInfo);
        creditPage.attentionUnderCVC("Неверный формат");
    }

    @Test
    void shouldDeclineTransactionWithDubleZeroMonth() {
        mainPage.creditPage();
        var validYear = Integer.parseInt(DataHelper.getCurrentYear()) + 1;
        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear("00", String.valueOf(validYear));
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.errorRejectedFromBank();
    }


    @Test
    void shouldShowAttentionTextOneFigureMonth() {
        mainPage.creditPage();
        var validYear = Integer.parseInt(DataHelper.getCurrentYear()) + 1;
        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear("1", String.valueOf(validYear));
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.attentionUnderMonth("Неверный формат");

    }

    @Test
    void shouldSuccessTransactionWithMaxAllowedDate() {
        mainPage.creditPage();
        var currentMonth = DataHelper.getCurrentMonth();
        var maxYear = Integer.parseInt(DataHelper.getCurrentYear()) + 5;
        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear(currentMonth, String.valueOf(maxYear));
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getCreditCardData().getStatus());
    }

    @Test
    void shouldSuccessTransactionWithMinAllowedDate() {
        mainPage.creditPage();
        var currentMonth = DataHelper.getCurrentMonth();
        var currentYear = DataHelper.getCurrentYear();
        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear(currentMonth, currentYear);
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getCreditCardData().getStatus());
    }

    @Test
    void shouldDeclineTransactionWithPastMonth() {
        mainPage.creditPage();
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
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.attentionUnderMonth("Неверно указан срок действия карты");
    }

    @Test
    void shouldDeclineTransactionWithPastYear() {
        mainPage.creditPage();
        var currentMonth = Integer.parseInt(DataHelper.getCurrentMonth());
        var pastYear = Integer.parseInt(DataHelper.getCurrentYear()) - 1;
        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear
                (String.valueOf(currentMonth), String.valueOf(pastYear));
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.attentionUnderYear("Истёк срок действия карты");
    }

    @Test
    void shouldDeclineTransactionWithInvalidMonth() {
        mainPage.creditPage();
        var currentYear = DataHelper.getCurrentYear();
        var cardInfo = DataHelper.approvedCardWithParametrizedMonthAndYear
                (String.valueOf("50"), currentYear);
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.attentionUnderMonth("Неверно указан срок действия карты");

    }

    @Test
    void shouldSuccessTransactionWithMaxAllowedDateMinusMonth() {
        mainPage.creditPage();
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
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getCreditCardData().getStatus());
    }

    @Test
    void shouldSuccessTransactionWithMaxLengthNameOwner() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedLengthOwner(30);
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getCreditCardData().getStatus());
    }

    @Test
    void shouldSuccessTransactionWithMinLengthNameOwner() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedLengthOwner(3);
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getCreditCardData().getStatus());
    }

    @Test
    void shouldDeclineTransactionWithNoValidMinLengthNameOwner() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedLengthOwner(2);
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.attentionUnderOwner("Корректно введите имя с платежной карты");
    }

    @Test
    void shouldDeclineTransactionWithNoValidMaxLengthNameOwner() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedLengthOwner(31);
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.attentionUnderOwner("Корректно введите имя с платежной карты");
    }

    @Test
    void shouldDeclineTransactionWithNameOwnerOnCyrillic() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedOwnerName("Петр Мамеев");
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.attentionUnderOwner("Корректно введите имя с платежной карты");
    }

    @Test
    void shouldApprovedTransactionWithNameOwnerWithDash() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedOwnerName("Bob-Jon");
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.successFromBank();
        assertEquals("APPROVED", SQLHelper.getCreditCardData().getStatus());
    }

    @Test
    void shouldDeclineTransactionWithNameOwnerWithNumbers() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedOwnerName("Ivan Ivan567ov");
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.attentionUnderOwner("Корректно введите имя с платежной карты");
    }

    @Test
    void shouldDeclineTransactionWithNameOwnerWithSpecialCharacters() {
        mainPage.creditPage();
        var cardInfo = DataHelper.generatedDataWithParametrizedOwnerName("Ivan ?№@$%");
        var creditPage = new CreditPage();
        creditPage.insertPayCreditCardData(cardInfo);
        creditPage.attentionUnderOwner("Корректно введите имя с платежной карты");
    }


}

