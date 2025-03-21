package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.Annotation.AutoFileAssign;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {
    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFileAssign(value = OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> select(DishPageQueryDTO dish);

    @Select("select * from dish where id = #{id}")
    Dish selectById(Long id);

    void deleteBatch(List<Long> ids);


    @Select("select * from dish where category_id = #{categoryId} and status = 1")
    List<DishVO> selectByCategoryId(Integer categoryId);

    @Select("select * from dish where id = #{id}")
    Dish getDishById(Long id);

    @AutoFileAssign(value = OperationType.UPDATE)
    void update(Dish dish);
}
