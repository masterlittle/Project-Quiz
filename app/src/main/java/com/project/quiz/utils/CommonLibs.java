package com.project.quiz.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Shitij on 23/09/15.
 */
public class CommonLibs {

    public static class TeamDetails {
        public static String TEAM_NUMBER = "teamNumber";
    }

    public static class Priority {

        /**
         * The interface Priority constants.
         */
        @Retention(RetentionPolicy.SOURCE)
        @IntDef({VERY_LOW, LOW, MEDIUM, HIGH, VERY_HIGH})
        public @interface PriorityConstants {
        }

        /**
         * The constant VERY_LOW.
         */
        public static final int VERY_LOW = 0;
        /**
         * The constant LOW.
         */
        public static final int LOW = 1;
        /**
         * The constant MEDIUM.
         */
        public static final int MEDIUM = 2;
        /**
         * The constant HIGH.
         */
        public static final int HIGH = 3;
        /**
         * The constant VERY_HIGH.
         */
        public static final int VERY_HIGH = 4;
    }

    public static class FragmentId{
        public static int ID_FRAGMENT_DISPLAY_STUDENTS=1;
        public static int ID_FRAGMENT_SELECT_STUDENTS=2;
        public static int ID_FRAGMENT_SELECT_TEAMS=3;
        public static int ID_FRAGMENT_HOME_SCREEN=4;
        public static int ID_FRAGMENT_DISTRIBUTE_STUDENTS=5;
        public static int ID_FRAGMENT_DISPLAY_TEAM_SCORES=6;
        public static int ID_FRAGMENT_FINISH_QUIZ=7;
        public static int ID_FRAGMENT_ALL_EVENTS=8;
        public static int ID_FRAGMENT_SIGN_IN=9;
        public static int ID_FRAGMENT_SIGN_UP=10;
        public static int ID_FRAGMENT_LOADING=11;
    }

    public static class SharedPrefsKeys{
        public static String TEAM_NUMBER = "team_number";
    }

    public static class Roles{
        public static String ROLE_ADMINISTRATOR = "Administrator";
        public static String ROLE_MODERATOR = "Moderator";
        public static String ROLE_NORMAL = "Normal";
    }



}
