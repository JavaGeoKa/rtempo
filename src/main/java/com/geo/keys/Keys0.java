package com.geo.keys;

import com.vdurmont.emoji.EmojiParser;

public enum Keys0 {


    B1 {
        public String toString() {
            return EmojiParser.parseToUnicode(":speech_balloon: Информация");
        }
    },
    B2 {
        public String toString() {
            return EmojiParser.parseToUnicode(":chart_with_upwards_trend: История");
        }
    },
    B3 {
        public String toString() {
            return EmojiParser.parseToUnicode(":briefcase: обратная связь");
        }
    }


    }

