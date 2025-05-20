package practice.shop.common;

import lombok.Getter;

public enum responseStatus {

    SUCCESS("Success"),
    ERROR("Error");

    @Getter
    private String message;

    responseStatus(String message){
        this.message = message;
    }

}
