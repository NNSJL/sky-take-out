package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品Id查询对应的套餐Id
     * @param dishIds
     * @return
     */
    List<Long> getSetMealIdByDishIds(List<Long> dishIds);

    /**
     * 批量保存套餐与菜品的关联关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id删除套餐与菜品的关联关系
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐id查询关联的菜品信息
     * @param SetmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{SetmealId}")
    List<SetmealDish> getBySetmealId(Long SetmealId);
}
