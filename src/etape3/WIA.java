package etape3;

public class WIA {

    private static boolean isServer = false;

    public static boolean getIsServer() {
        return isServer;
    }

    public static void setServer(boolean isServer) {
        WIA.isServer = isServer;
    }
}
