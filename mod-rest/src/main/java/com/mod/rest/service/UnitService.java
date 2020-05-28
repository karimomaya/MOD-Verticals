package com.mod.rest.service;

import com.mod.rest.model.Unit;
import com.mod.rest.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by MinaSamir on 05/27/2019.
 */
@Service
public class UnitService {

    @Autowired
    UnitRepository unitRepository;

    public HashMap<String, HashMap<String, String>> getUnitNamesMultilingual() {
        HashMap<String, HashMap<String, String>> returnedStructure = new HashMap<>();
        List<Unit> returnedUnits = unitRepository.getAllUnits();

        for (Unit unit: returnedUnits) {
            HashMap<String, String> langSpecific = new HashMap<>();
            langSpecific.put("ar", unit.getUnitNameByLanguage("ar"));
            langSpecific.put("en", unit.getUnitNameByLanguage("en"));
            returnedStructure.put(unit.getUnitCode(), langSpecific);
        }
        return returnedStructure;
    }

    public Unit getUnitByUnitCode(String unitCode){
        List<Unit> returnedUnit = unitRepository.getUnitByUnitCode(unitCode);
        if(returnedUnit.size() == 1){
            return returnedUnit.get(0);
        }
        return new Unit();
    }

    public void substituteUnitCodes(List<?> objectList, String unitCodeColumnName, String lang){
        HashMap<String, HashMap<String, String>> allUnits = getUnitNamesMultilingual();

        Class<?> cls = objectList.get(0).getClass();
        BeanInfo info = null;
        Method getter = null;
        Method setter = null;
        try {
            info = Introspector.getBeanInfo(cls);

            PropertyDescriptor[] props = info.getPropertyDescriptors();

            for (PropertyDescriptor prop: props) {
                if(prop.getName().equals(unitCodeColumnName)) {
                    getter = cls.getDeclaredMethod(prop.getReadMethod().getName());
                    setter = cls.getDeclaredMethod(prop.getWriteMethod().getName(), String.class);
                    break;
                }
            }

            for (Object obj : objectList) {
                String langSpecificUnitName = allUnits.get( (String) getter.invoke(obj) ).get(lang);
                setter.invoke(obj, langSpecificUnitName);
            }

        } catch (IntrospectionException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
