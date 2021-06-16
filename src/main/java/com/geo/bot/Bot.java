package com.geo.bot;


import com.geo.dao.PersonRepositoty;
import com.geo.dto.TempDto;
import com.geo.keys.Keys0;
import com.geo.keys.KeysBegin;
import com.geo.model.Person;
import com.vdurmont.emoji.EmojiParser;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Bot extends TelegramLongPollingBot {

    @Autowired
    private PersonRepositoty personRepositoty;




//keyboard
    List<String> keys = new ArrayList<>();
    public StringBuilder answer = new StringBuilder();


    private Map<Long, Person> mapPersons = DataStarter.mapPersons;
    Person currentPerson = new Person();

    private SendMessage mailingMessage = new SendMessage();



        //Input section --------------------------------
        @SneakyThrows
        @Override
        public void onUpdateReceived(Update update) {
            SendMessage message = new SendMessage();

                if(update.hasMessage()) {
                    message.setChatId(update.getMessage().getChatId());
                    HashMap<String, Callable<SendMessage>> map = new HashMap<String, Callable<SendMessage>>() {
                        {
                            put("/start", () -> {
                                return methodStart(update, message);
                            });

                            put("В начало", () -> {
                                return methodStart(update, message);
                            });
                            put(EmojiParser.parseToUnicode(":speech_balloon: Информация"), () -> {
                                return methodInfo(update, message);
                            });
                            put(EmojiParser.parseToUnicode(":chart_with_upwards_trend: История"), () -> {
                                return methodHistory(update, message);
                            });
                            put(EmojiParser.parseToUnicode(":briefcase: обратная связь"), () -> {
                                return methodFeedback(update, message);
                            });



                        }
                    };
                    try {
                        sendAnswer(map.get(update.getMessage().getText()).call());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                }

            System.out.println("Commands not recognized");

            }




//  METHODS----------------------------------------------------------------------------

// /start
    private SendMessage methodStart(Update update, SendMessage message) {
        //keys
        keys.clear();
        Keys0 arr[] = Keys0.values();
        for (Keys0 i : arr) {
            keys.add(i.toString());
        }
        addKeys(message, keys);

        //logic
            if (mapPersons.containsKey(update.getMessage().getChatId())) {
                currentPerson = mapPersons.get(update.getMessage().getChatId());
            } else {
                currentPerson = new Person();
                currentPerson.setRequests(0);
                currentPerson.setId(update.getMessage().getChatId());
                currentPerson.setFirstName(update.getMessage().getFrom().getFirstName() == null ?
                        "undefined" : update.getMessage().getFrom().getFirstName());
                currentPerson.setLastName(update.getMessage().getFrom().getLastName() == null ?
                        "undefined" : update.getMessage().getFrom().getLastName());
                currentPerson.setConnectTime(LocalDateTime.now());


                mapPersons.put(update.getMessage().getChatId(), currentPerson);
                personRepositoty.save(currentPerson);
//                log.info("Save new user " + currentPerson.getId());


            }

            //Prepare first message
            answer.setLength(0);
            answer.append("Добро пожаловать в Remote Temperature Control System \n");
            message.setText(answer.toString());
            return message;

        }

//  info
    private SendMessage methodInfo(Update update, SendMessage message) {
        //keys
        keys.clear();
        KeysBegin arr[] = KeysBegin.values();
        for (KeysBegin i : arr) {
            keys.add(i.toString());
        }
        addKeys(message, keys);

        //logic

        //Prepare first message
        answer.setLength(0);
        answer.append("Система предназначена для работы с контроллерами ESP32 based  \n");
        message.setText(answer.toString());
        return message;

    }

//  info
    private SendMessage methodHistory(Update update, SendMessage message) {
        //keys
        keys.clear();
        KeysBegin arr[] = KeysBegin.values();
        for (KeysBegin i : arr) {
            keys.add(i.toString());
        }
        addKeys(message, keys);

        //logic

        //Prepare first message
        answer.setLength(0);
        answer.append("История изменения климата за последние 24 часа  \n");
        message.setText(answer.toString());
        return message;

    }

//  info
    private SendMessage methodFeedback(Update update, SendMessage message) {
        //keys
        keys.clear();
        KeysBegin arr[] = KeysBegin.values();
        for (KeysBegin i : arr) {
            keys.add(i.toString());
        }
        addKeys(message, keys);

        //logic

        //Prepare first message
        answer.setLength(0);
        answer.append("Оставить отбратнуб связь и пожелания Вы можете здесь: @MisterG00DMan");
        message.setText(answer.toString());
        return message;

    }

//mailing
    public void sendTemperatureFromBot(TempDto tempDto) {

        answer.setLength(0);
        answer.append("dev id: " + tempDto.getSerialDeviceId())
                .append("\ngroup id: " + tempDto.getGroupId())
                .append("\ntemperature: " + tempDto.getTemperature())
                .append("\ntime: " + tempDto.getMeterageTime())
                .append("\nhost" + tempDto.getHost());
        mailingMessage.setText(answer.toString());

        mapPersons.keySet().forEach(id -> {
            mailingMessage.setChatId(id);
            sendAnswer(mailingMessage);
        });

    }

// "SIMPLE"

//Save Current Parameters
//    private void saveCurrentParameters(Long chatId) {
//
//        currentPerson.setLastUseTime(LocalDateTime.now());
//        Integer i = currentPerson.getRequests();
//        currentPerson.setRequests(i);
//
//        personRepositoty.updateLastRequestTime(chatId, currentPerson.getLastUseTime());
//        personRepositoty.updateRequests(chatId, currentPerson.getRequests());
//
//
//    }











    // KEYS ------------------------------------------------------------------------
        private void addKeys(SendMessage message, List<String> keys) {
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            message.setReplyMarkup(replyKeyboardMarkup);
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);

//        //Create list keyboard rows
            List<KeyboardRow> keyboardRows = new ArrayList<KeyboardRow>();
//
//        //Calculate keyboard rows for keys
            Integer indexButton = 0;
            KeyboardRow firstRow = new KeyboardRow();
            KeyboardRow secondRow = new KeyboardRow();
            KeyboardRow thirdRow = new KeyboardRow();
            KeyboardRow fourthRow = new KeyboardRow();
            KeyboardRow fiveRow = new KeyboardRow();




            for (String k : keys) {

                if (indexButton == 0 || indexButton ==1) {
                    firstRow.add(k);
                } else if (indexButton == 2 || indexButton ==3) {
                    secondRow.add(k);
                } else if (indexButton == 4 || indexButton ==5) {
                    thirdRow.add(k);
                }
                indexButton++;
            }

            keyboardRows.add(firstRow);
            keyboardRows.add(secondRow);
            keyboardRows.add(thirdRow);
            keyboardRows.add(fourthRow);
            keyboardRows.add(fiveRow);


            replyKeyboardMarkup.setKeyboard(keyboardRows);


        }

        //SendAnswer general
        private void sendAnswer(SendMessage message) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                System.out.println("===========================executeException");
            }
        }






        //Bot service data
        @Value("${bot.name}")
        private String botUsername;

        @Value("${bot.token}")
        private String botToken;

        public String getBotUsername() {
            return botUsername;
        }

        public String getBotToken() {
            return botToken;
        }





}

