package com.jeeplus.modules.data.web;


import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.data.service.YybMusicianAlbumService;
import com.jeeplus.modules.musician.entity.YybMusicianAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/data")
public class DataController  extends BaseController {
    @Autowired
    private YybMusicianAlbumService yybMusicianAlbumService;
    /**
     * 音乐人列表数据
     */
    @ResponseBody
    @RequestMapping(value = "albumData")
    public Map<String, Object> albumData(YybMusicianAlbum yybMusicianAlbum, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<YybMusicianAlbum> page = yybMusicianAlbumService.findPage(new Page<YybMusicianAlbum>(request, response), yybMusicianAlbum);
        return getBootstrapData(page);
    }
}
