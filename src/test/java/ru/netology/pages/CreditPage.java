package ru.netology.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class CreditPage {
    private SelenideElement heading = Selenide.$x("//h3[text()='Кредит по данным карты']");
    private SelenideElement cardNumber = Selenide.$x("//span[text()='Номер карты']/following-sibling::span/input");
    private SelenideElement month = Selenide.$x("//span[text()='Месяц']/following-sibling::span/input");
    private SelenideElement year = Selenide.$x("//span[text()='Год']/following-sibling::span/input");
    private SelenideElement owner = Selenide.$x("//span[text()='Владелец']/following-sibling::span/input");
    private SelenideElement cvc = Selenide.$x("//span[text()='CVC/CVV']/following-sibling::span/input");
    private SelenideElement proceedButton = Selenide.$x("//span[text()='Продолжить']");
    private SelenideElement errorRejected = Selenide.$x("//div[text()='Ошибка!" + " Банк отказал в проведении операции.']");
    private SelenideElement success = Selenide.$x("//div[text()='Успешно']");

    private SelenideElement attentionCardNumberField = Selenide.$x("//span[text()='Номер карты']" +
            "/following-sibling::span[@class='input__sub']");
    private SelenideElement attentionMonthField = Selenide.$x("//span[text()='Месяц']" +
            "/following-sibling::span[@class='input__sub']");
    private SelenideElement attentionYearField = Selenide.$x("//span[text()='Год']" +
            "/following-sibling::span[@class='input__sub']");
    private SelenideElement attentionCardOwnerField = Selenide.$x("//span[text()='Владелец']" +
            "/following-sibling::span[@class='input__sub']");
    private SelenideElement attentionCvcField = Selenide.$x("//span[text()='CVC/CVV']" +
            "/following-sibling::span[@class='input__sub']");

    public CreditPage() {
        heading.shouldBe(visible);
    }

    public void insertPayCreditCardData(DataHelper.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        month.setValue(cardInfo.getMonth());
        year.setValue(cardInfo.getYear());
        owner.setValue(cardInfo.getCardOwner());
        cvc.setValue(cardInfo.getCvc());
        proceedButton.click();
    }

    public void errorRejectedFromBank() {
        errorRejected.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void successFromBank() {
        success.shouldBe(visible, Duration.ofSeconds(15));
    }
    public void clickProceedButton() {
        proceedButton.click();
    }
    public void insertPayCardWithEmptyCardNumber(DataHelper.CardInfo cardInfo) {
        month.setValue(cardInfo.getMonth());
        year.setValue(cardInfo.getYear());
        owner.setValue(cardInfo.getCardOwner());
        cvc.setValue(cardInfo.getCvc());
        clickProceedButton();
    }

    public void insertPayCardWithEmptyMonth(DataHelper.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        year.setValue(cardInfo.getYear());
        owner.setValue(cardInfo.getCardOwner());
        cvc.setValue(cardInfo.getCvc());
        clickProceedButton();
    }

    public void insertPayCardEmptyYear(DataHelper.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        month.setValue(cardInfo.getMonth());
        owner.setValue(cardInfo.getCardOwner());
        cvc.setValue(cardInfo.getCvc());
        clickProceedButton();
    }

    public void insertPayCardEmptyOwner(DataHelper.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        month.setValue(cardInfo.getMonth());
        year.setValue(cardInfo.getYear());
        cvc.setValue(cardInfo.getCvc());
        clickProceedButton();
    }

    public void insertPayCardEmptyCVC(DataHelper.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        month.setValue(cardInfo.getMonth());
        year.setValue(cardInfo.getYear());
        owner.setValue(cardInfo.getCardOwner());
        clickProceedButton();
    }

    public void attentionUnderNumberCard(String attentionText) {
        attentionCardNumberField.shouldBe(visible);
        attentionCardNumberField.shouldHave(text(attentionText));
    }

    public void attentionUnderMonth(String attentionText) {
        attentionMonthField.shouldBe(visible);
        attentionMonthField.shouldHave(text(attentionText));
    }

    public void attentionUnderYear(String attentionText) {
        attentionYearField.shouldBe(visible);
        attentionYearField.shouldHave(text(attentionText));
    }

    public void attentionUnderOwner(String attentionText) {
        attentionCardOwnerField.shouldBe(visible);
        attentionCardOwnerField.shouldHave(text(attentionText));
    }

    public void attentionUnderCVC(String attentionText) {
        attentionCvcField.shouldBe(visible);
        attentionCvcField.shouldHave(text(attentionText));
    }



}
