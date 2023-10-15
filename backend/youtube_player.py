import datetime

from pytubefix import YouTube, Search

import video_config


class YouTubePlayer:

    def set_yt_video_information(self, video):
        video_config.title = video.title
        video_config.channel = video.author
        video_config.views = video.views
        video_config.duration = str(datetime.timedelta(seconds=video.length))
        video_config.thumbnail_url = video.thumbnail_url

    def search_video(seld, query: str):
        search = Search(query).results
        return search

    def get_yt_video_information(self):
        return {
            "title": video_config.title,
            "channel": video_config.channel,
            "views": video_config.views,
            "duration": video_config.duration,
            "thumbnail_url": video_config.thumbnail_url
        }

    def get_yt_video_information_from_obj(self, video: YouTube):
        return {
            "title": video.title,
            "channel": video.author,
            "views": video.views,
            "duration": video.length,
            "thumbnail_url": video.thumbnail_url,
        }

    def clear_video_information(self):
        video_config.title = ""
        video_config.channel = ""
        video_config.views = ""
        video_config.duration = ""
        video_config.thumbnail_url = ""
