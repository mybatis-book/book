package tk.mybatis.web.mapper;

import org.apache.ibatis.session.RowBounds;
import tk.mybatis.web.model.SysDict;

import java.util.List;

/**
 * @author liuzh
 */
public interface DictMapper {

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    SysDict selectByPrimaryKey(Long id);

    /**
     * 条件查询
     *
     * @param sysDict
     * @return
     */
    List<SysDict> selectBySysDict(SysDict sysDict, RowBounds rowBounds);

    /**
     * 新增
     *
     * @param sysDict
     * @return
     */
    int insert(SysDict sysDict);

    /**
     * 根据主键更新
     *
     * @param sysDict
     * @return
     */
    int updateById(SysDict sysDict);

    /**
     * 根据主键删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);
}
