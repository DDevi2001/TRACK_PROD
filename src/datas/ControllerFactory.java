package datas;

public class ControllerFactory {
    public static <T> T getController(Class<T> clazz) throws ClassCastException {
        Object manager = new DataManager();
        if (clazz.isInstance(manager)) {
            return clazz.cast(manager);
        } else {
            throw new ClassCastException("Cannot cast");
        }
    }
}
