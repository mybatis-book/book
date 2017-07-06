package tk.mybatis.web.service;

import tk.mybatis.web.model.SysDict;

import java.util.List;

/**
 * @author liuzh
 */
public interface DictService {

    SysDict findById(Long id);

    List<SysDict> findBySysDict(SysDict sysDict, Integer offset, Integer limit);

    boolean saveOrUpdate(SysDict sysDict);

    boolean deleteById(Long id);
}
