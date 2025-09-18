package com.dailycodework.shopping_cart.Helper.Filter;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ProfanityFilter {
//    Arrays.asList có thể thay đổi giá trị phần tử còn List.of thì không (bất biến)
    private static final List<String> PROFANE_WORDS = Arrays.asList(
            "clm", "caclonme", "conloz", "conlozme", "cacloz",
            "dmm", "ditme", "ditmemay", "ditconme", "dume",
            "cha mày", "cha m" , "chame", "chame may", "chame no", "chamay",
            "dkm", "dcm", "dkmd", "dcmn", "dkmm",
            "loz", "lol", "lolz", "lolme", "lozme",
            "cac", "cacdit", "caclo", "caclon", "cacditme",
            "duma", "dume", "dumemo", "dumemay",
            "dit", "ditcu", "ditcon", "ditbo", "ditbu",
            "motherfucker", "fuck", "fuckyou", "fuckoff", "bullshit",
            "bitch", "asshole", "shit", "piss", "damn",
            "cứt", "cứt mẹ", "cứt bố",
            "đéo", "đếch", "đệt", "địt", "đụ",
            "đụ má", "đụ mẹ", "đụ cha", "đụ bà", "đụ ông",
            "ngu", "nguvcl", "nguvl", "ngucomo", "nguchet",
            "vcl", "vl", "vler", "vcc", "vcd",
            "cặt", "cặc", "cak", "cac", "kac","cc","ccc",
            "lon", "lol", "lz", "loz", "lòn","lồn","cl",
            "cái l", "cái lon", "cái loz", "cái lz", "cái lìn",
            "buồi", "bùi", "bui", "buoi",
            "chó", "chó má", "chó đẻ", "chó chết", "chó điên",
            "mẹ mày", "mẹ m", "mẹ kiếp", "mẹ đĩ", "mẹ bà",
            "thằng l", "thằng ngu", "thằng chó", "thằng điên", "thằng khùng"
    );
    //Danh sách các trang thương mại điện tử bị chặn
    private static final List<Pattern> BLOCK_LINK_DOMAINS = List.of(
            // Các dịch vụ rút gọn link
            Pattern.compile("(?i)bit\\.ly"),
            Pattern.compile("(?i)goo\\.gl"),
            Pattern.compile("(?i)t\\.co"),
            Pattern.compile("(?i)tinyurl\\.com"),
            Pattern.compile("(?i)ow\\.ly"),
            // Các trang thương mại điện tử
            Pattern.compile("(?i)shopee"),
            Pattern.compile("(?i)lazada\\.vn"),
            Pattern.compile("(?i)tiki\\.vn"),
            Pattern.compile("(?i)sendo\\.vn"),
            Pattern.compile("(?i)amazon\\.com"),
            Pattern.compile("(?i)dienmayxanh\\.com"),
            Pattern.compile("(?i)chotot\\.com"),
            Pattern.compile("(?i)fptshop\\.com"),
            Pattern.compile("(?i)cellphones\\.com"),
            Pattern.compile("(?i)nguyenkim\\.com"),
            Pattern.compile("(?i)thanhungfutsal\\.com"),
            Pattern.compile("(?i)ebay\\.com"),
            Pattern.compile("(?i)aliexpress\\.com"),
            Pattern.compile("(?i)taobao\\.com"),

            // Các trang mạng xã hội và ứng dụng
            Pattern.compile("(?i)tiktok\\.com"),
            Pattern.compile("(?i)facebook\\.com"),
            Pattern.compile("(?i)fb\\.com"),
            Pattern.compile("(?i)zalo\\.me"),
            Pattern.compile("(?i)zaloapp\\.com"),
            Pattern.compile("(?i)instagram\\.com"),
            Pattern.compile("(?i)twitter\\.com"),
            Pattern.compile("(?i)youtube\\.com"),
            Pattern.compile("(?i)youtu\\.be"),
            Pattern.compile("(?i)telegram\\.org"),
            Pattern.compile("(?i)t\\.me"),
            Pattern.compile("(?i)whatsapp\\.com"),
            Pattern.compile("(?i)wechat\\.com"),
            Pattern.compile("(?i)line\\.me"),
            Pattern.compile("(?i)discord\\.com"),
            Pattern.compile("(?i)reddit\\.com"),
            Pattern.compile("(?i)pinterest\\.com"),
            Pattern.compile("(?i)linkedin\\.com"),

            // Các trang affiliate và quảng cáo
            Pattern.compile("(?i)affiliate\\."),
            Pattern.compile("(?i)ref="),
            Pattern.compile("(?i)aff_id="),
            Pattern.compile("(?i)partner_id="),
            Pattern.compile("(?i)clickbank\\.net")
    );
    public static boolean filterProfanity (String input){
        String lowercaseInput = input.toLowerCase();
        for(String ProfanityWord : PROFANE_WORDS){
            if(lowercaseInput.contains(ProfanityWord)){
                return true; // Phát hiện từ tục tĩu
            }
        }
        return false; // Không phát hiện từ tục tĩu
    }
    public static boolean isBlockLink(String input){
        return BLOCK_LINK_DOMAINS.stream().anyMatch(pattern -> pattern.matcher(input).find());
    }

}
