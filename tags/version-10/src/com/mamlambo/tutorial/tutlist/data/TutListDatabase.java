/*
 * Copyright (c) 2011, Lauren Darcey and Shane Conder
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this list of 
 *   conditions and the following disclaimer.
 *   
 * * Redistributions in binary form must reproduce the above copyright notice, this list 
 *   of conditions and the following disclaimer in the documentation and/or other 
 *   materials provided with the distribution.
 *   
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific prior 
 *   written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * <ORGANIZATION> = Mamlambo
 */
package com.mamlambo.tutorial.tutlist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TutListDatabase extends SQLiteOpenHelper {
    private static final String DEBUG_TAG = "TutListDatabase";
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "tutorial_data";

    public static final String TABLE_TUTORIALS = "tutorials";
    public static final String ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_URL = "url";

    private static final String CREATE_TABLE_TUTORIALS = "CREATE TABLE "
            + TABLE_TUTORIALS + " (" + ID
            + " integer PRIMARY KEY AUTOINCREMENT, " + COL_TITLE
            + " text NOT NULL, " + COL_URL + " text UNIQUE NOT NULL);";

    private static final String DB_SCHEMA = CREATE_TABLE_TUTORIALS;

    public TutListDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_SCHEMA);
        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DEBUG_TAG, "Upgrading database. Existing contents will be lost. ["
                + oldVersion + "]->[" + newVersion + "]");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUTORIALS);
        onCreate(db);
    }

    /**
     * Create sample data to use
     * 
     * @param db
     *            The open database
     */
    private void seedData(SQLiteDatabase db) {
        db.execSQL("insert into tutorials (title, url) values ('Best of Tuts+ in February 2011', 'http://mobile.tutsplus.com/articles/news/best-of-tuts-in-february-2011/');");
        db.execSQL("insert into tutorials (title, url) values ('Design & Build a 1980s iOS Phone App: Design Comp Slicing', 'http://mobile.tutsplus.com/tutorials/mobile-design-tutorials/80s-phone-app-slicing/');");
        db.execSQL("insert into tutorials (title, url) values ('Create a Brick Breaker Game with the Corona SDK: Game Controls', 'http://mobile.tutsplus.com/tutorials/corona/create-a-brick-breaker-game-with-the-corona-sdk-game-controls/');");
        db.execSQL("insert into tutorials (title, url) values ('Exporting Graphics for Mobile Apps: PNG or JPEG?', 'http://mobile.tutsplus.com/tutorials/mobile-design-tutorials/mobile-design_png-or-jpg/');");
        db.execSQL("insert into tutorials (title, url) values ('Android Tablet Design', 'http://mobile.tutsplus.com/tutorials/android/android-tablet-design/');");
        db.execSQL("insert into tutorials (title, url) values ('Build a Titanium Mobile Pizza Ordering App: Order Form Setup', 'http://mobile.tutsplus.com/tutorials/appcelerator/build-a-titanium-mobile-pizza-ordering-app-order-form-setup/');");
        db.execSQL("insert into tutorials (title, url) values ('Create a Brick Breaker Game with the Corona SDK: Application Setup', 'http://mobile.tutsplus.com/tutorials/corona/corona-sdk_brick-breaker/');");
        db.execSQL("insert into tutorials (title, url) values ('Android Tablet Virtual Device Configurations', 'http://mobile.tutsplus.com/tutorials/android/android-sdk_tablet_virtual-device-configuration/');");
        db.execSQL("insert into tutorials (title, url) values ('Build a Titanium Mobile Pizza Ordering App: Topping Selection', 'http://mobile.tutsplus.com/tutorials/appcelerator/pizza-ordering-app-part-2/');");
        db.execSQL("insert into tutorials (title, url) values ('Design & Build a 1980s iOS Phone App: Interface Builder Setup', 'http://mobile.tutsplus.com/tutorials/iphone/1980s-phone-app_interface-builder-setup/');");
    }
}
