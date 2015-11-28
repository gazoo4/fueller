package sk.berops.android.fueller.dataModel;

/**
 * Created by bernard.halas on 08/11/2015.
 *
 * Interface to forcing the class which is implementing this interface to contain an ID
 */
public interface Identifiable {

    /**
     * Return an unique object ID
     * @return Object ID
     */
    public long getId();
}
