package com.example.shopit;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private final ProductModel[] initialProducts = {
            new ProductModel(
                    "G4M3R ELITE i7-12700K/32GB/2TB/RTX3070Ti/W11x",
                    "Lorem Ipsum jest tekstem stosowanym jako przykładowy wypełniacz w przemyśle poligraficznym. Został po raz pierwszy użyty w XV w. przez nieznanego drukarza do wypełnienia tekstem próbnej książki. Pięć wieków później zaczął być używany przemyśle elektronicznym, pozostając praktycznie niezmienionym. Spopularyzował się w latach 60. XX w. wraz z publikacją arkuszy Letrasetu, zawierających fragmenty Lorem Ipsum, a ostatnio z zawierającym różne wersje Lorem Ipsum oprogramowaniem przeznaczonym do realizacji druków na komputerach osobistych, jak Aldus PageMaker",
                    7999.99,
                    R.drawable.pc
            ),

            new ProductModel(
                    "Dell Inspiron G15 5511 i5-11400H/16GB/512/Win11 RTX3050",
                    "Poznaj laptopa Dell G15, następcę kultowych modeli G3 i G5, o 15-calowym ekranie i odświeżaniu do 120 Hz. Model ten precyzyjnie łączy przeznaczenie do pracy, czy nauki z pasjami oraz czasem wolnym. W szczególności odpowiada na wymagające potrzeby fanów gamingu.",
                    4499.99,
                        R.drawable.pc
            ),
            new ProductModel(
                    "Apple iPad Pro 12,9 M1 256 GB 5G Space Gray",
                    "W lekkiej, smukłej i eleganckiej konstrukcji Apple iPad Pro 12,9” kryje się potężna moc nowoczesnego procesora M1. Wykorzystaj ją do tworzenia skomplikowanych projektów graficznych i jednocześnie ciesz się niesamowitym obrazem, jaki wyświetla ekran Liquid Retina XDR. Dzięki wydajności, szybkości oraz płynności działania praca i rozrywka z Apple iPad Pro 12,9” to czysta przyjemność",
                    6399.99,
                    R.drawable.tablet
            ),
            new ProductModel(
                    "Lenovo Yoga Tab 13 QS870/8GB/128/Android 11 WiFi",
                    "Z tabletem Lenovo Yoga Tab 13 Twoje doznania multimedialne będą jeszcze bogatsze. Zagłębiaj się w różne światy dzięki technologii Dolby Vision™ i otaczaj się dźwiękiem w jakości kinowej Dolby Atmos® płynącym z czterech głośników JBL wspomaganych dodatkowo przez system Lenovo Premium Audio.",
                    2990.99,
                    R.drawable.tablet
            ),
            new ProductModel(
                    "Logitech G102 LIGHTSYNC czarna",
                    "Logitech G102 Lightsync to przewodowa mysz z klasyczną 6-przyciskową, wygodną konstrukcję w kolorze czarnym. Urządzenie wyposażono w czujnik przeznaczony do gier. Co więcej, mysz posiada podświetlenie Lightsync, które nie tylko sprawi, że gra będzie bardziej wciągająca, ale również podświetli Twoje biurko.\n" +
                            "\n" +
                            "Sprawdź, jak Logitech G102 Lightsync Czarna wygląda w rzeczywistości. Chwyć zdjęcie poniżej i przeciągnij je w lewo lub prawo aby obrócić produkt lub skorzystaj z przycisków nawigacyjnych.",
                    99.99,
                    R.drawable.mouse
            ),
            new ProductModel(
                    "Logitech G502 HERO",
                    "Myszka gamingowa Logitech G502 HERO to potężna mysz do gier, która oferuje cały zestaw funkcji dla graczy. Zaawansowany czujnik optyczny zapewniający maksymalną precyzję śledzenia ruchów, podświetlenie RGB z możliwością dostosowania, niestandardowe profile gier i ciężarki z możliwością zmiany pozycji. To wszystko sprawia, że Mysz do gier G502 HERO jest odpowiednim narzędziem, aby podjąć wyzwanie.\n" +
                            "\n" +
                            "Sprawdź, jak produkt wygląda w rzeczywistości. Chwyć zdjęcie poniżej i przeciągnij je w lewo lub prawo aby obrócić produkt lub skorzystaj z przycisków nawigacyjnych.",
                    399.99,
                    R.drawable.mouse
            ),
            new ProductModel(
                    "SteelSeries Apex 3 TKL",
                    "Przygotuj się do gry w najlepszy możliwy sposób z klawiaturą SteelSeries Apex 3 TKL. Zaprojektowano ją tak, aby łączyła w sobie nowoczesne technologie z użytecznymi funkcjonalnościami gamingowymi. Dzięki temu każda rozgrywka dostarczy Ci niesamowitych wrażeń. Klawiatura wyposażona jest w przełączniki zmniejszające tarcie, gwarantujące ponadprzeciętną trwałość. Zamów swój egzemplarz i wejdź do gry",
                    249.99,
                    R.drawable.keyobard
            ),
            new ProductModel(
                    "SPC Gear GK630K Tournament Kailh Blue RGB",
                    "O zwycięstwie decydują nie tylko umiejętności, ale także odpowiedni sprzęt. Zapewnij sobie właściwe zaplecze technologiczne i wybierz gamingową klawiaturę SPC Gear GK630K Tournament. Wytrzymała konstrukcja i szybkie czasy reakcji tworzą idealne narzędzie do wielogodzinnych sesji z ulubioną grą. Postaw na SPC Gear GK630K Tournament i przechyl szalę zwycięstwa na swoją stronę.",
                    99.99,
                    R.drawable.keyobard
            )
    };



    private Context context;
    public static final String DATABASE_NAME = "ShopIT.db";
    public static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(Model.createSchema(ProductModel.class));
            db.execSQL(Model.createSchema(CustomerModel.class));
            db.execSQL(Model.createSchema(CartModel.class));
            db.execSQL(Model.createSchema(CartRelationModel.class));

            for(ProductModel product : initialProducts)
                db.insert(ProductModel.TABLE, null, product.constructCV());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(Model.dropSchema(ProductModel.class));
            db.execSQL(Model.dropSchema(CartModel.class));
            db.execSQL(Model.dropSchema(CustomerModel.class));
            db.execSQL(Model.dropSchema(CartRelationModel.class));



        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        onCreate(db);
    }

}
