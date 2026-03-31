package org.zzu.schoolimsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zzu.schoolimsystem.dto.ChatSendDTO;
import org.zzu.schoolimsystem.entity.EventChatRecord;
import org.zzu.schoolimsystem.entity.SysUser;
import org.zzu.schoolimsystem.mapper.EventChatRecordMapper;
import org.zzu.schoolimsystem.mapper.SysUserMapper;
import org.zzu.schoolimsystem.service.ChatService;
import org.zzu.schoolimsystem.vo.ChatMessageVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final EventChatRecordMapper eventChatRecordMapper;
    private final SysUserMapper sysUserMapper;  // 新增注入





    @Override
    public List<ChatMessageVO> getHistory(Long orderId) {
        LambdaQueryWrapper<EventChatRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EventChatRecord::getOrderId, orderId)
                .orderByAsc(EventChatRecord::getSendTime)
                .orderByAsc(EventChatRecord::getId);

        List<EventChatRecord> records = eventChatRecordMapper.selectList(queryWrapper);
        return records.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatMessageVO sendMessage(Long orderId, ChatSendDTO dto) {
        EventChatRecord record = new EventChatRecord();
        record.setOrderId(orderId);
        record.setSenderId(dto.getSenderId());
        record.setReceiverId(dto.getReceiverId());
        record.setContentType(dto.getContentType());
        record.setContent(dto.getContent());
        record.setExtra(dto.getExtra());
        record.setIsRead(0);
        record.setSendTime(LocalDateTime.now());

        eventChatRecordMapper.insert(record);

        return toVO(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long orderId, String receiverId) {
        LambdaUpdateWrapper<EventChatRecord> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(EventChatRecord::getOrderId, orderId)
                .eq(EventChatRecord::getReceiverId, receiverId)
                .eq(EventChatRecord::getIsRead, 0)
                .set(EventChatRecord::getIsRead, 1);

        eventChatRecordMapper.update(null, updateWrapper);
    }

    private ChatMessageVO toVO(EventChatRecord record) {
        ChatMessageVO vo = new ChatMessageVO();
        BeanUtils.copyProperties(record, vo);

        // 查询发送者信息
        SysUser sender = sysUserMapper.selectById(record.getSenderId());
        if (sender != null) {
            // 优先用昵称，没有则用用户名
            String name = sender.getNickname() != null && !sender.getNickname().isEmpty()
                    ? sender.getNickname()
                    : sender.getUsername();
            vo.setSenderName(name);
            vo.setSenderAvatar(sender.getAvatar());
        } else {
            vo.setSenderName("用户" + record.getSenderId());
        }

        // 查询接收者信息
        SysUser receiver = sysUserMapper.selectById(record.getReceiverId());
        if (receiver != null) {
            String name = receiver.getNickname() != null && !receiver.getNickname().isEmpty()
                    ? receiver.getNickname()
                    : receiver.getUsername();
            vo.setReceiverName(name);
        } else {
            vo.setReceiverName("用户" + record.getReceiverId());
        }

        return vo;
    }
}
