package com.real_estate.eqlt;

import com.real_estate.eqlt.config.BotConfig;
import com.real_estate.eqlt.repos.RealEstateRepo;
import com.real_estate.eqlt.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    @Autowired
    private RealEstateRepo realEstateRepo;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if ("/start".equals(messageText)) {
                startCommand(chatId, update.getMessage().getChat().getFirstName());
            } else if ("/end".equals(messageText)) {
                endCommand(chatId, update.getMessage().getChat().getFirstName());
            }
            sendRealEstateCommand(chatId);
        } else if (update.hasCallbackQuery()) {
            CallbackQuery query = update.getCallbackQuery();
            String callBackData = query.getData();
            long chatId = query.getMessage().getChatId();
            switch (callBackData) {
                case "Подобрать недвижимость", "Связаться с администратором" -> {
                    sendRealEstateType(chatId);
                    System.out.println(callBackData);
                }
            }
            switch (callBackData) {
                case "Аппартаменты", "Пентхаус", "Таунхаус", "Вилла", "Земельный участок" -> {
                    sendRealEstateRegionChoice(chatId);
                    System.out.println(callBackData);
                }
            }
            switch (callBackData) {
                case "Интерка", "Минск-Мир", "Каменка" -> {
                    sendPriceCommand(chatId);
                    System.out.println(callBackData);
                }
            }
            switch (callBackData) {
                case "40 гривен", "50 баксов", "100 рублей", "5 Никит", "0,00002 евра" -> {
                    sendRealEstateReadiness(chatId);
                    System.out.println(callBackData);
                }
            }
            switch (callBackData) {
                case "Готовая", "На этапе строительства", "Неважно" -> {
                    sendRealEstateWayToPay(chatId);
                    System.out.println(callBackData);
                }
            }
            switch (callBackData) {
                case "Сразу", "В рассрочку" -> {
                    System.out.println(callBackData);
                }
            }
        }

    }

    private void startCommand(Long chatId, String username) throws TelegramApiException {
        String answer = "Hi, " + username + ", nice to meet you!" + "\n";
        sendMessage(chatId, answer);
    }

    private void endCommand(Long chatId, String username) throws TelegramApiException {
        String answer = "Goodbye, " + username + ", thank you for using our bot!";
        sendMessage(chatId, answer);
    }

    private void sendRealEstateWayToPay(Long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Как рассчитываться собираешься?");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button1 = new ArrayList<>();
        List<InlineKeyboardButton> button2 = new ArrayList<>();
        InlineKeyboardButton selectRealEstateWayToPay1 = new InlineKeyboardButton();
        InlineKeyboardButton selectRealEstateWayToPay2 = new InlineKeyboardButton();
        selectRealEstateWayToPay1.setText("Сразу");
        selectRealEstateWayToPay1.setCallbackData("Сразу");
        selectRealEstateWayToPay2.setText("В рассрочку");
        selectRealEstateWayToPay2.setCallbackData("В рассрочку");
        button1.add(selectRealEstateWayToPay1);
        button2.add(selectRealEstateWayToPay2);
        buttons.add(button1);
        buttons.add(button2);
        inlineKeyboardMarkup.setKeyboard(buttons);
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramApiException("Cannot send message");
        }
    }

    private void sendRealEstateReadiness(Long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Тип готовности недвижимости");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button1 = new ArrayList<>();
        List<InlineKeyboardButton> button2 = new ArrayList<>();
        List<InlineKeyboardButton> button3 = new ArrayList<>();
        InlineKeyboardButton selectRealEstateWayToPay1 = new InlineKeyboardButton();
        InlineKeyboardButton selectRealEstateWayToPay2 = new InlineKeyboardButton();
        InlineKeyboardButton selectRealEstateWayToPay3 = new InlineKeyboardButton();
        selectRealEstateWayToPay1.setText("Готовая");
        selectRealEstateWayToPay1.setCallbackData("Готовая");
        selectRealEstateWayToPay2.setText("На этапе строительства");
        selectRealEstateWayToPay2.setCallbackData("На этапе строительства");
        selectRealEstateWayToPay3.setText("Неважно");
        selectRealEstateWayToPay3.setCallbackData("Неважно");
        button1.add(selectRealEstateWayToPay1);
        button2.add(selectRealEstateWayToPay2);
        button3.add(selectRealEstateWayToPay3);
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        inlineKeyboardMarkup.setKeyboard(buttons);
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramApiException("Cannot send message");
        }
    }

    private void sendRealEstateCommand(Long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Давай выбирать");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button1 = new ArrayList<>();
        List<InlineKeyboardButton> button2 = new ArrayList<>();
        InlineKeyboardButton selectRealEstateButton1 = new InlineKeyboardButton();
        InlineKeyboardButton selectRealEstateButton2 = new InlineKeyboardButton();
        selectRealEstateButton1.setText("Подобрать недвижимость");
        selectRealEstateButton2.setText("Связаться с администратором");
        selectRealEstateButton1.setCallbackData("Подобрать недвижимость");
        selectRealEstateButton2.setCallbackData("Связаться с администратором");
        button1.add(selectRealEstateButton1);
        button2.add(selectRealEstateButton2);
        buttons.add(button1);
        buttons.add(button2);
        inlineKeyboardMarkup.setKeyboard(buttons);
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramApiException("Cannot send message");
        }
    }

    private void sendRealEstateRegionChoice(Long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Где бы недвижимость хотел?");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button1 = new ArrayList<>();
        List<InlineKeyboardButton> button2 = new ArrayList<>();
        List<InlineKeyboardButton> button3 = new ArrayList<>();
        InlineKeyboardButton realEstateRegion1 = new InlineKeyboardButton();
        InlineKeyboardButton realEstateRegion2 = new InlineKeyboardButton();
        InlineKeyboardButton realEstateRegion3 = new InlineKeyboardButton();
        realEstateRegion1.setText("Интерка");
        realEstateRegion2.setText("Минск-Мир");
        realEstateRegion3.setText("Каменка");
        realEstateRegion1.setCallbackData("Интерка");
        realEstateRegion2.setCallbackData("Минск-Мир");
        realEstateRegion3.setCallbackData("Каменка");
        button1.add(realEstateRegion1);
        button2.add(realEstateRegion2);
        button3.add(realEstateRegion3);
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        inlineKeyboardMarkup.setKeyboard(buttons);
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramApiException("Cannot send message");
        }
    }

    private void sendRealEstateType(Long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Что будем брать ?");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button1 = new ArrayList<>();
        List<InlineKeyboardButton> button2 = new ArrayList<>();
        List<InlineKeyboardButton> button3 = new ArrayList<>();
        List<InlineKeyboardButton> button4 = new ArrayList<>();
        List<InlineKeyboardButton> button5 = new ArrayList<>();
        InlineKeyboardButton realEstateButton1 = new InlineKeyboardButton();
        InlineKeyboardButton realEstateButton2 = new InlineKeyboardButton();
        InlineKeyboardButton realEstateButton3 = new InlineKeyboardButton();
        InlineKeyboardButton realEstateButton4 = new InlineKeyboardButton();
        InlineKeyboardButton realEstateButton5 = new InlineKeyboardButton();
        realEstateButton1.setText("Аппартаменты");
        realEstateButton2.setText("Пентхаус");
        realEstateButton3.setText("Таунхаус");
        realEstateButton4.setText("Вилла");
        realEstateButton5.setText("Земельный участок");
        realEstateButton1.setCallbackData("Аппартаменты");
        realEstateButton2.setCallbackData("Пентхаус");
        realEstateButton3.setCallbackData("Таунхаус");
        realEstateButton4.setCallbackData("Вилла");
        realEstateButton5.setCallbackData("Земельный участок");
        button1.add(realEstateButton1);
        button2.add(realEstateButton2);
        button3.add(realEstateButton3);
        button4.add(realEstateButton4);
        button5.add(realEstateButton5);
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        buttons.add(button5);
        inlineKeyboardMarkup.setKeyboard(buttons);
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramApiException("Cannot send message");
        }
    }

    private void sendPriceCommand(Long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("По какой цене будем брать?");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button1 = new ArrayList<>();
        List<InlineKeyboardButton> button2 = new ArrayList<>();
        List<InlineKeyboardButton> button3 = new ArrayList<>();
        List<InlineKeyboardButton> button4 = new ArrayList<>();
        List<InlineKeyboardButton> button5 = new ArrayList<>();
        InlineKeyboardButton priceButton1 = new InlineKeyboardButton();
        InlineKeyboardButton priceButton2 = new InlineKeyboardButton();
        InlineKeyboardButton priceButton3 = new InlineKeyboardButton();
        InlineKeyboardButton priceButton4 = new InlineKeyboardButton();
        InlineKeyboardButton priceButton5 = new InlineKeyboardButton();
        priceButton1.setText("40 гривен");
        priceButton2.setText("50 баксов");
        priceButton3.setText("100 рублей");
        priceButton4.setText("5 Никит");
        priceButton5.setText("0,00002 евра");
        priceButton1.setCallbackData("40 гривен");
        priceButton2.setCallbackData("50 баксов");
        priceButton3.setCallbackData("100 рублей");
        priceButton4.setCallbackData("5 Никит");
        priceButton5.setCallbackData("0,00002 евра");
        button1.add(priceButton1);
        button2.add(priceButton2);
        button3.add(priceButton3);
        button4.add(priceButton4);
        button5.add(priceButton5);
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        buttons.add(button5);
        inlineKeyboardMarkup.setKeyboard(buttons);
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramApiException("Cannot send message");
        }
    }

    private void sendMessage(Long chatId, String textToSend) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new TelegramApiException("Cannot send message");
        }
    }
}
