package com.lx.SongJoyHub.client.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.SongJoyHub.client.dao.entity.SongDO;
import com.lx.SongJoyHub.client.dto.req.SongFuzzyInquiryReqDTO;
import com.lx.SongJoyHub.client.dto.resp.SongQueryRespDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 歌曲数据存储层
 */
@Mapper
public interface SongMapper extends BaseMapper<SongDO> {
    /**
     * 多条件更新歌曲信息
     */
    int updateSong(@Param("song") SongDO song);

    /**
     * 根据歌手查询歌曲
     * @param singerList 歌手列表
     * @param num 查询数量
     * @return 歌曲列表
     */
    List<SongDO> selectSongBySinger(@Param("singerList") List<String> singerList, @Param("num") int num);

    /**
     * 根据语种和类型查询歌曲
     * @param languagesList 语种
     * @param songTypeList 类型
     * @param num 数量
     * @return 歌曲列表
     */
    List<SongDO> selectSongByLanguagesAndType(@Param("languagesList") List<String> languagesList,@Param("songTypeList") List<String> songTypeList,@Param("num") int num);

    /**
     * 分页查询歌曲信息
     * @param page 页码
     * @param pageSize 页大小
     * @return 歌曲信息
     */
    List<SongQueryRespDTO> pageQuerySong(@Param("page") Integer page, @Param("pageSize") Integer pageSize);

    /**
     * 多条件分页查询歌曲信息
     * @param requestParam 请求参数
     * @return 歌曲信息
     */
    List<SongQueryRespDTO> fuzzyInquiry(@Param("req") SongFuzzyInquiryReqDTO requestParam);

    /**
     * 恢复正常状态
     */
    int restoreSongStatus(@Param("songId") Long songId);
}
