package org.zzu.schoolimsystem.mapper;

/**
 * ClassName: ChatMessageMapper
 * Package: org.zzu.schoolimsystem.mapper
 * Description:
 *
 * @Author gly
 * @Create 2026/2/11 23:21
 * @Version 1.0
 */


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.zzu.schoolimsystem.entity.EventChatRecord;

@Mapper
public interface EventChatRecordMapper extends BaseMapper<EventChatRecord> {
}
