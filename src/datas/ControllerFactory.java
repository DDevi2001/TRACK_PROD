package datas;

public class ControllerFactory {
    public static <T, U> T getController(Class<T> clazz, Class<U> claz) throws ClassCastException, NullPointerException {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            if (element.getClassName().equals(claz.getName())) {
                Object manager = new DataManager();
                if (clazz.isInstance(manager)) {
                    return clazz.cast(manager);
                } else {
                    throw new ClassCastException("Cannot cast");
                }
            }
        }
        return null;
    }
}
