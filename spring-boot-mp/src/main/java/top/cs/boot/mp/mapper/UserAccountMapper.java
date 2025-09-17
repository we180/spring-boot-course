package top.cs.boot.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.cs.boot.mp.entity.UserAccount;

@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {
}
