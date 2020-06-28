package com.mod.rest.service;

import com.mod.rest.model.Lookup;
import com.mod.rest.repository.LookupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MinaSamir on 05/27/2019.
 */
@Service
public class LookupService {

    @Autowired
    LookupRepository lookupRepository;

    public HashMap<String, HashMap<String, HashMap<String, String>>> getLookupValuesByCategory(String category) {
        HashMap<String, HashMap<String, HashMap<String, String>>> returnedStructure = new HashMap<>();
        HashMap<String, HashMap<String, String>> idWithLang = new HashMap<>();
        HashMap<String, String> langSpecific = new HashMap<>();

        List<Lookup> returnedLookups = lookupRepository.getLookupByCategory(category);

        for (Lookup lookup: returnedLookups) {
            langSpecific.put("ar", lookup.getValueByLanguage("ar"));
            langSpecific.put("en", lookup.getValueByLanguage("en"));
            idWithLang.put(lookup.getKey(), langSpecific);
            langSpecific = new HashMap<>();
        }
        returnedStructure.put(category, idWithLang);
        return returnedStructure;
    }

    public void substituteLookupIds(List<?> objectList, String category, String lookupIdColumnName, String lang){
        HashMap<String, HashMap<String, HashMap<String, String>>> allLookups = getLookupValuesByCategory(category);

        Class<?> cls = objectList.get(0).getClass();
        BeanInfo info = null;
        Method getter = null;
        Method setter = null;
        try {
            info = Introspector.getBeanInfo(cls);

            PropertyDescriptor[] props = info.getPropertyDescriptors();

            for (PropertyDescriptor prop: props) {
                if(prop.getName().equals(lookupIdColumnName)) {
                    try {
                        getter = cls.getDeclaredMethod(prop.getReadMethod().getName());
                    } catch (NoSuchMethodException | NullPointerException e) {
                        System.out.println("Couldn't retrieve Getter for the LookupService");
                    }
                    try {
                        setter = cls.getDeclaredMethod(prop.getWriteMethod().getName(), String.class);
                    } catch (NoSuchMethodException | NullPointerException e) {
                        System.out.println("Couldn't retrieve Setter for the LookupService");
                    }
                    break;
                }
            }

            for (Object obj : objectList) {
                try {
                    String langSpecificLookupName = allLookups.get(category).get((String) getter.invoke(obj)).get(lang);
                    setter.invoke(obj, langSpecificLookupName);
                } catch (NullPointerException e) {
                    System.out.println("Error occurred when calling the Getter/Setter Function in the LookupService");
                }
            }

        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
