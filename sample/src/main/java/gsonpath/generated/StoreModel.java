package gsonpath.generated;

import com.google.gson.annotations.SerializedName;
import gsonpath.annotation.AutoGsonAdapter;

import java.util.List;

@AutoGsonAdapter(rootField = "store")
class StoreModel {
    @SerializedName("book")
    List<BookModel> bookList;

    @SerializedName("bicycle.color")
    String bikeColour;

    @SerializedName("bicycle.price")
    double bikePrice;

    @AutoGsonAdapter
    static class BookModel {
        public String category;
        public String author;
        public String title;
        public double price;
    }
}
