import os
import datetime

import video_config
from moviepy.video.io.VideoFileClip import VideoFileClip


class LocalPlayer():
    def __init__(self, path):
        self.path = path

    def set_video_information(self, video_path: str):
        title, format = os.path.splitext(os.path.basename(video_path))

        video_config.title = title
        video_config.channel = ""
        video_config.views = ""
        video_config.duration = str(datetime.timedelta(seconds=int(VideoFileClip(video_path).duration)))
        video_config.thumbnail_url = ""

    def get_video_information(self):
        return {
            "title": video_config.title,
            "channel": video_config.channel,
            "views": video_config.views,
            "duration": video_config.duration,
            "thumbnail_url": video_config.thumbnail_url
        }

    # return list of files
    def list_video_files(self):
        video_files = []
        for file in os.listdir(self.path):
            video_files.append(file)
        return video_files
