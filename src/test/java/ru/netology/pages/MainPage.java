package ru.netology.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;

public class MainPage {
    private SelenideElement heading = Selenide.$x("//h2[text()='Путешествие дня']");
    private SelenideElement paymentButton = Selenide.$x("//span[text()='Купить']");
    private SelenideElement creditButton = Selenide.$x("//span[text()='Купить в кредит']");

    public MainPage() {
        heading.shouldBe(visible);
    }

    public PayPage payPage() {
        paymentButton.click();
        return new PayPage();
    }

    public CreditPage creditPage() {
        creditButton.click();
        return new CreditPage();
    }


}
