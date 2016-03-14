/********************************************************
 * Copyright (C) 2012 Ezetap Mobile Solutions Pvt. Ltd.
 * <p/>
 * This software is distributed  on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 *******************************************************/
package com.eze.api;

import com.ezetap.sdk.EzetapApiConfig;

public class EzetapUserConfig {

    /**
     * Variable used to save prepare device at initialize or not
     * */
    private static boolean prepareDevice = false;
    /**
     * Variable used to save user config once the initialize is successful
     * */
    private static EzetapApiConfig ezeUserConfig = null;
    /**
     * Variable used to save user name once the initialize is successful
     * */
    private static String userName = null;

    private EzetapUserConfig() {
    }

    public static boolean getPrepareDevice() {
        return prepareDevice;
    }

    public static void setPrepareDevice(boolean prepareDevice) {
        EzetapUserConfig.prepareDevice = prepareDevice;
    }

    /**
     * Method to get the saved config.
     * */
    public static EzetapApiConfig getEzeUserConfig() {
        return ezeUserConfig;
    }

    /**
     * Method to set the saved config.
     * @param EzetapApiConfig ezeUserConfig
     * 										- User config passed as part of initialize API
     * */
    public static void setEzeUserConfig(EzetapApiConfig ezeUserConfig) {
        EzetapUserConfig.ezeUserConfig = ezeUserConfig;
    }

    /**
     * Method to get the saved user name.
     * */
    public static String getUserName() {
        return userName;
    }

    /**
     * Method to set the user name.
     * @param String userName
     * 						 - Name of the user
     * */
    public static void setUserName(String userName) {
        EzetapUserConfig.userName = userName;
    }
}
