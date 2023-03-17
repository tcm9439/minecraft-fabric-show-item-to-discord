package com.maisyt.minecraft.util.Exception;

public class IllegalCallerException {
    public static class IllegalCallFromClientException extends IllegalStateException {
        public IllegalCallFromClientException(){
            super("Cannot call this method from client side.");
        }
    }

    public static class IllegalCallFromServerException extends IllegalStateException {
        public IllegalCallFromServerException(){
            super("Cannot call this method from server side.");
        }
    }

    public static class IllegalCallFromMultiplayerClientException extends IllegalStateException {
        public IllegalCallFromMultiplayerClientException(){
            super("Cannot call this method from client in multiplayer (online) mode.");
        }
    }

    public static class IllegalCallFromLocalClientException extends IllegalStateException {
        public IllegalCallFromLocalClientException(){
            super("Cannot call this method from client in local single player mode.");
        }
    }
}