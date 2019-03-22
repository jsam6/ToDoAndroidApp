package com.example.mcflurry.notes.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "notes") //Declaring the class as an Entity for Room.
public class Note implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="title")
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    // @NonNull if u don't want the field to be Null.
    @ColumnInfo(name = "timestamp")
    public String timestamp;



    public Note(String title, String content, String timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    @Ignore //Ignore empty constructor. bcs its only used on selected situation
    public Note() {
    }


    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        timestamp = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    //************************ PARCELABLE ********************************//
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(timestamp);
    }
    //************************ PARCELABLE ********************************//

}

//************************ PARCELABLE ********************************//
//    protected Note(Parcel in) {
//        title = in.readString();
//        content = in.readString();
//        timestamp = in.readString();
//    }
//
//    public static final Creator<Note> CREATOR = new Creator<Note>() {
//        @Override
//        public Note createFromParcel(Parcel in) {
//            return new Note(in);
//        }
//
//        @Override
//        public Note[] newArray(int size) {
//            return new Note[size];
//        }
//    };

//    @Override
//    public int describeContents() {
//        return 0;
//    }

//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(title);
//        parcel.writeString(content);
//        parcel.writeString(timestamp);
//    }
//************************ PARCELABLE ********************************//