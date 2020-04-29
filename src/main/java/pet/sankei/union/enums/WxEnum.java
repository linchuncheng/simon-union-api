package pet.sankei.union.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Jensen Lam
 * @date 2020-03-05 23:48
 **/

public interface WxEnum {
    String MsgType_TEXT = "text";
    String MsgType_EVENT = "event";
    String MsgType_IMAGE = "image";
    String MsgType_VOICE = "voice";
    String MsgType_VIDEO = "video";
    String MsgType_SHORT_VIDEO = "shortvideo";
    String MsgType_LOCATION = "location";
    String MsgType_LINK = "link";

    @AllArgsConstructor
    @Getter
    enum Event {
        subscribe("subscribe"),
        unsubscribe("unsubscribe");
        String msg;
    }

    @AllArgsConstructor
    @Getter
    enum ResultCode {
        errCode("errcode");
        String msg;
    }

}
