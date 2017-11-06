package com.hfad.starbuzz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class CourseCategoryActivity extends Activity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_category);
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        ListView listCourses = (ListView) findViewById(R.id.list_courses);
        try {
            db = starbuzzDatabaseHelper.getReadableDatabase();
            cursor = db.query("COURSES",
                    new String[]{"_id", "PREFIX ||''|| COURSENUMBER"},
                    null, null, null, null, null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"PREFIX ||''|| COURSENUMBER"},
                    new int[]{android.R.id.text1},
                    0);
            listCourses.setAdapter(listAdapter);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        //Create the listener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> listCourses,
                                            View itemView,
                                            int position,
                                            long id) {
                        //Pass the course the user clicks on to CourseActivity
                        Intent intent = new Intent(CourseCategoryActivity.this,
                                CourseActivity.class);
                        intent.putExtra(CourseActivity.EXTRA_COURSEID, (int) id);
                        startActivity(intent);
                    }
                };

        //Assign the listener to the list view
        listCourses.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
