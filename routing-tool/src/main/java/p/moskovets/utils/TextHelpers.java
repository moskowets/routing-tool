package p.moskovets.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TextHelpers {

    public final static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
}
