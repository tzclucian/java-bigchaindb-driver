/*
 * (C) Copyright 2017 Authenteq (https://authenteq.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Bohdan Bezpartochnyi <bohdan@authenteq.com>
 */

package com.authenteq.util;

import net.i2p.crypto.eddsa.EdDSAPublicKey;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;

public class DriverUtils {
    private static final char[] DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getHex(byte[] data) {
        final int l = data.length;
        final char[] outData = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            outData[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            outData[j++] = DIGITS[0x0F & data[i]];
        }

        return new String(outData);
    }

    /*
    We are using a hack to make stardard org.json be automatically sorted
    by key desc alphabetically
     */
    public static JSONObject makeSelfSorting(JSONObject input) {
        if (input == null)
            return null;

        JSONObject json = new JSONObject();
        Field map = null;
        try {
            map = json.getClass().getDeclaredField("map");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (map == null) {
            return json;
        }

        map.setAccessible(true);//because the field is private final...
        try {
            map.set(json, new TreeMap<>());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        map.setAccessible(false);

        Iterator<String> flavoursIter = input.keys();
        while (flavoursIter.hasNext()){
            String key = flavoursIter.next();
            try {
                json.put(key, input.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return json;
    }

    /*
    We need to sort the keys in alphabetical order to sign the transaction successfully.
     */
    public static JSONObject getSelfSortingJson() {
        JSONObject json = makeSelfSorting(new JSONObject());
        return json;
    }

    public static String convertToBase58(EdDSAPublicKey publicKey) {
        return Base58.encode(Arrays.copyOfRange(publicKey.getEncoded(), 12, 44));
    }
}
