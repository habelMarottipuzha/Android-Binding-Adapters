# Android Recycler Adapter with Databinding(Chat and List support)[![Build Status](https://travis-ci.org/gotev/recycler-adapter.svg?branch=master)](https://travis-ci.org/gotev/recycler-adapter) [![Javadocs](http://javadoc.io/badge/net.gotev/recycleradapter.svg)](http://javadoc.io/doc/net.gotev/recycleradapter) <a href="http://www.methodscount.com/?lib=net.gotev%3Arecycleradapter%3A1.5.3"></a>

 This library helps to simplify the burden of writing adapter class for every RecyclerView.
 It also reduces chance for getting errors.  
 
 Standard `RecyclerView.Adapter` is tedious to work with, because you have to write repetitive code and to concentrate all your items view logic and binding into the adapter itself, which is really bad. This library was born to be able to have the following for each element in a recycler view:
 
 * an XML layout file with data-binding capability, in which to define the item's view hierarchy
 * a model class, in which to specify the binding between the model and the view and in which to handle user interactions with the item.
 
 In this way every item of the recycler view has its own set of files, resulting in a cleaner and easier to maintain code base.
 

 # Index
 * [Setup](#setup)
 * [RecyclerView List Adapter](#recycler_list_adapter)
 * [Android Recycler Chat Adapter with Databinding   ](#recycler_chat_adapter)

 
 ## <a name="setup"></a>Setup
##### Step 1. Add it in your root build.gradle at the end of repositories:
 ```groovy
     allprojects {
            repositories {
                ...
                maven { url 'https://jitpack.io' }
            }
        }
 ``` 
 
 ##### Step 2. Add the dependency
 
 ```groovy
    compile 'com.github.habelMarottipuzha:Android-binding-adapters:v1.0.0.1'
 ```
  
 <hr/>
 
 
#<a name="recycler_list_adapter"></a> RecyclerView List Adapter 
 ## Basic usage tutorial
 ### 1. Declare the RecyclerView
 In your layout resource file or where you want the `RecyclerView` (e.g. `activity_listview.xml`) add the following:
 ```xml
     <android.support.v7.widget.RecyclerView
         android:id="@+id/recycler_view"
         android:scrollbars="vertical"
         android:layout_width="match_parent"
         android:layout_height="wrap_content" />
 ```
 
 ### 2. Create your item layout
 Create your item layout (e.g. `item_event.xml`). For example:
 ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <layout xmlns:android="http://schemas.android.com/apk/res/android">
    
        <data>
    
            <variable
                name="event"
                type="in.habel.EventModel" />
        </data>
    
        <LinearLayout
            android:id="@+id/layout1"
            style="@style/event_container">
    
            <TextView
                style="@style/event_name"
                android:text="@{event.event,default=`Meet John `}" />
    
            <TextView
                style="@style/event_time"
                android:text="@{event.eventDate,default=`Mon, 21 jan`}" />
        </LinearLayout>
    </layout> 
 ```
 
 ### 3. Create the Model class
 ```java
    public class EventModel {
        private String event;
        private String eventDate;
    
        public String getEvent() {
            return event;
        }
    
        public void setEvent(String event) {
            this.event = event;
        }
    
        public String getEventDate() {
            return eventDate;
        }
    
        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }
    
    }

 ```
 
 ### 4. Instantiate Adapter and add items
 In your Activity (`onCreate` method) or Fragment (`onCreateView` method):
 ```java
 
    // Declare globally  
    RecyclerAdapter<EventModel, ItemEventBinding> adapter;
    
    // inside method
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    // set recyclerview props here if needed
    ArrayList<EventModel> dataSet = getData();
    adapter = new RecyclerAdapter(recyclerView, dataSet, R.layout.item_event,
            new RecyclerCallback<ItemEventBinding, EventModel>() {
    
                @Override
                public void bindData(ItemEventBinding binder, EventModel model) {
                    binder.setEvent(model);
                    // dynamic changes to view can be performed here
                }
            });
 ```
 
 ##</a>Refreshing / Updating adapter data
 You can insert new data into the recycler view by 
 ```java
    adapter.insert(eventModel); // will add to bottom of list
    adapter.insert(eventModel,position);
    adapter.insert(eventModelList); //will add each to the bottom

 ``` 
 If you want to update entire dataSet by adding,removing or updating dataSet.
 ```java
    adapter.refresh(dataSet); 
 ```
 This will keep items in new dataSet and remove all other data without resetting the adapter.Make sure to implement `equals()` and `hashCode()` methods of `EventModel` class.
 
 
 Checkout the sample app provided to get a real example in action.
 
 <hr>
 <hr> 
 
 # <a name="recycler_chat_adapter"></a>Android Recycler Chat Adapter with Databinding   
 
  ## Basic usage tutorial
  ### 1. Declare the RecyclerView
  In your layout resource file or where you want the `RecyclerView` (e.g. `activity_listview.xml`) add the following:
  ```xml
      <android.support.v7.widget.RecyclerView
          android:id="@+id/recycler_view"
          android:scrollbars="vertical"
          android:layout_width="match_parent"
          android:layout_height="wrap_content" />
  ```
  
  ### 2. Create your item layout
  Create your Incoming chat item layout (e.g. `list_chat_incoming.xml`). For example:
  ```xml
     <?xml version="1.0" encoding="utf-8"?>
     <layout xmlns:android="http://schemas.android.com/apk/res/android"
         xmlns:app="http://schemas.android.com/apk/res-auto">
     
         <data>
     
             <import type="in.habel.chat_adapters.DateHelper" />
     
             <variable
                 name="chat"
                 type="in.habel.chat_adapters.DemoChatModel" />
         </data>
     
         <LinearLayout
             android:id="@+id/layout1"
             android:layout_width="match_parent"
             android:layout_height="wrap_content"
             android:orientation="horizontal"
             android:weightSum="1">
     
             <android.support.constraint.ConstraintLayout style="@style/chatbox.incoming">
     
                 <TextView
                     android:id="@+id/txtDisplayName"
                     style="@style/chat.name"
                     android:text="@{chat.user,default=`David `}"
                     app:layout_constraintLeft_toLeftOf="parent"
                     app:layout_constraintTop_toTopOf="parent" />
     
                 <TextView
                     android:id="@+id/txtDisplayDate"
                     style="@style/chat.time"
                     android:text="@{DateHelper.getFormattedDate(chat.addedOn),default=`21-05, 8.00 am`}"
                     app:layout_constraintBottom_toBottomOf="@+id/txtDisplayName"
                     app:layout_constraintHorizontal_bias="0.1"
                     app:layout_constraintLeft_toRightOf="@+id/txtDisplayName"
                     app:layout_constraintRight_toRightOf="parent"
                     app:layout_constraintTop_toTopOf="parent" />
     
                 <TextView
                     android:id="@+id/textView"
                     style="@style/chat.text"
                     android:text="@{chat.message,default=@string/default_chat_message}"
                     app:layout_constraintLeft_toLeftOf="parent"
                     app:layout_constraintTop_toBottomOf="@+id/txtDisplayName" />
             </android.support.constraint.ConstraintLayout>
     
             <View style="@style/chatbox_space" />
         </LinearLayout>
     </layout>
  ```
    Create your Outgoing chat item layout (e.g. `list_chat_incoming.xml`). For example:
 
  ```xml
     <?xml version="1.0" encoding="utf-8"?>
     <layout xmlns:android="http://schemas.android.com/apk/res/android"
         xmlns:app="http://schemas.android.com/apk/res-auto">
     
         <data>
     
             <import type="in.habel.chat_adapters.DateHelper" />
     
             <variable
                 name="chat"
                 type="in.habel.chat_adapters.DemoChatModel" />
         </data>
     
         <LinearLayout
             android:id="@+id/layout1"
             android:layout_width="match_parent"
             android:layout_height="wrap_content"
             android:orientation="horizontal"
             android:weightSum="1">
     
             <View style="@style/chatbox_space" />
     
             <android.support.constraint.ConstraintLayout style="@style/chatbox.outgoing">
     
                 <TextView
                     android:id="@+id/txtDisplayName"
                     style="@style/chat.name"
                     android:text=""
                     app:layout_constraintLeft_toLeftOf="parent"
                     app:layout_constraintTop_toTopOf="parent" />
     
                 <TextView
                     android:id="@+id/txtDisplayDate"
                     style="@style/chat.time"
                     android:text="@{DateHelper.getFormattedDate(chat.addedOn),default=`5:12 pm`}"
                     app:layout_constraintBottom_toBottomOf="@+id/txtDisplayName"
                     app:layout_constraintHorizontal_bias="0.0"
                     app:layout_constraintLeft_toRightOf="@+id/txtDisplayName"
                     app:layout_constraintRight_toRightOf="parent"
                     app:layout_constraintTop_toTopOf="parent" />
     
                 <TextView
                     android:id="@+id/textView"
                     style="@style/chat.text"
                     android:text="@{chat.message,default=@string/default_chat_message}"
                     app:layout_constraintLeft_toLeftOf="parent"
                     app:layout_constraintTop_toBottomOf="@+id/txtDisplayName" />
             </android.support.constraint.ConstraintLayout>
         </LinearLayout>
     </layout>
  ```
  
  ### 3. Create the Model class (Make sure to implement `chatInterface` )
  ```java
     public class DemoChatModel implements chatInterface {
         private boolean isOut;
         private String message;
         private String user;
         private long addedOn;
         public DemoChatModel(String message, long addedOn) {
             this.message = message;
             this.addedOn = addedOn;
         }
     
         public DemoChatModel(String message, String user, long addedOn) {
             this.message = message;
             this.user = user;
             this.addedOn = addedOn;
         }
     
         public void setIsOut(boolean isOut) {
             this.isOut = isOut;
         }
     
         public String getMessage() {
             return message;
         }
     
         public void setMessage(String message) {
             this.message = message;
         }
     
         public long getAddedOn() {
             return addedOn;
         }
     
         public void setAddedOn(long addedOn) {
             this.addedOn = addedOn;
         }
     
         @Override
         public boolean isOutgoing() {
             return isOut;
         }
     
         public String getUser() {
             return user;
         }
     
         public void setUser(String user) {
             this.user = user;
         }
     
     
         @Override
         public boolean equals(Object o) {
             if (this == o) return true;
             if (o == null || getClass() != o.getClass()) return false;
     
             DemoChatModel that = (DemoChatModel) o;
     
             if (isOut != that.isOut) return false;
             if (addedOn != that.addedOn) return false;
             if (!message.equals(that.message)) return false;
             return user != null ? user.equals(that.user) : that.user == null;
     
         }
     
         @Override
         public int hashCode() {
             int result = (isOut ? 1 : 0);
             result = 31 * result + message.hashCode();
             result = 31 * result + (user != null ? user.hashCode() : 0);
             result = 31 * result + (int) (addedOn ^ (addedOn >>> 32));
             return result;
         }
     }
  ```
  This will really help if you are using data change listeners like in Realm. ie; if you dont know which data changed where, you have to just pass the whole dataset, the adapter will do the rest.
  ### 4. Instantiate Adapter and add items
  In your Activity (`onCreate` method) or Fragment (`onCreateView` method):
  ```java
      // Declare globally  
      RecyclerAdapter<DemoChatModel, ListItemIncoming,ListItemOutgoing> adapter;
      
      // inside method
      RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
      // set recyclerview props here if needed
      ArrayList<DemoChatModel> dataSet = getData();
         adapter = new RecyclerAdapter(recyclerView, dataSet,
                          R.layout.item_event,
                          new RecyclerCallback<  ListItemIncoming,ListItemOutgoing, EventModel>() {
      
                              @Override
                              public void bindData(  ListItemIncoming binderIn,ListItemOutgoing binderOut, DemoChatModel model) {
                                if(binderIn!=null){
                                   // this is an incoming chat
                                   binder.setChat(model);
                                 } 
                                 else {
                                   // this is an outgoing chat
                                      binder.setChat(model);
                                 }
                                 
                                 // dynamic changes to view can be performed here
                              }
      
      
                              @Override
                              public void onUnreadMessageFound(int totalMessages, int unreadMessages) {
                                // Under development
                              }
                          });
  ```
  
  ## Refreshing / Updating adapter data
  You can insert new data into the recycler view by 
  ```java
     adapter.insert(demoChatModel); // will add to bottom of list
     adapter.insert(demoChatModel,position);
     adapter.insert(demoChatModelList); //will add each to the bottom
 
  ``` 
  If you want to update entire dataSet by adding,removing or updating dataSet.
  ```java
     adapter.refresh(dataSet); 
  ```
  This will keep items in new dataSet and remove all other data without resetting the adapter.Make sure to implement `equals()` and `hashCode()` methods of `EventModel` class.
  
  
  Checkout the sample app provided to get a real example in action.
   