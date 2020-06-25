package com.mod.rest.service;

import com.mod.rest.model.Role;
import com.mod.rest.repository.RoleRepository;
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
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public HashMap<String, HashMap<String, String>> getRoleNamesMultilingual() {
        HashMap<String, HashMap<String, String>> returnedStructure = new HashMap<>();
        List<Role> returnedRoles = roleRepository.getAllRoles();

        for (Role role: returnedRoles) {
            HashMap<String, String> langSpecific = new HashMap<>();
            langSpecific.put("ar", role.getRoleNameByLanguage("ar"));
            langSpecific.put("en", role.getRoleNameByLanguage("en"));
            returnedStructure.put(role.getRoleCode(), langSpecific);
        }
        return returnedStructure;
    }

    public Role getRoleByRoleCode(String roleCode){
        List<Role> returnedRole = roleRepository.getRoleByRoleCode(roleCode);
        if(returnedRole.size() == 1){
            return returnedRole.get(0);
        }
        return new Role();
    }

    public void substituteRoleCodes(List<?> objectList, String roleCodeColumnName, String lang){
        HashMap<String, HashMap<String, String>> allRoles = getRoleNamesMultilingual();

        Class<?> cls = objectList.get(0).getClass();
        BeanInfo info = null;
        Method getter = null;
        Method setter = null;
        try {
            info = Introspector.getBeanInfo(cls);

            PropertyDescriptor[] props = info.getPropertyDescriptors();

            for (PropertyDescriptor prop: props) {
                if(prop.getName().equals(roleCodeColumnName)) {
                    try {
                        getter = cls.getDeclaredMethod(prop.getReadMethod().getName());
                    } catch (NoSuchMethodException | NullPointerException e) {
                        System.out.println("Couldn't retrieve Getter for the RoleService");
                    }
                    try {
                        setter = cls.getDeclaredMethod(prop.getWriteMethod().getName(), String.class);
                    } catch (NoSuchMethodException | NullPointerException e) {
                        System.out.println("Couldn't retrieve Setter for the RoleService");
                    }
                    break;
                }
            }

            for (Object obj : objectList) {
                try {
                    String langSpecificRoleName = allRoles.get((String) getter.invoke(obj)).get(lang);
                    setter.invoke(obj, langSpecificRoleName);
                } catch (NullPointerException e) {
                    System.out.println("Error occurred when calling the Getter/Setter Function in the RoleService");
                }
            }

        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
