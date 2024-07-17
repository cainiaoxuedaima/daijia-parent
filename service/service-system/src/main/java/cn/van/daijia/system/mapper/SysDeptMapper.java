package cn.van.daijia.system.mapper;

import cn.van.daijia.model.entity.system.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysDeptMapper extends BaseMapper<SysDept> {


}
