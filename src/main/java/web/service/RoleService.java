package web.service;

import web.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    List<Role> getAllRoles();

    Role getRoleByName(String name);

    Set<Role> getSetOfRoles(String[] roleNames);
}
