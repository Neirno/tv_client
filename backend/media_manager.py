import vlc
import threading
import time
import queue
import datetime
from functools import wraps
import config
import youtube_player
import video_config
from LocalPlayer import LocalPlayer  # Тут исправлен путь импорта
from pytubefix import YouTube
from functools import wraps

# Этот декоратор проверяет, доступны ли медиа-файлы перед вызовом функции
def media_available(func):
    @wraps(func)
    def wrapper(self, *args, **kwargs):
        if not self.is_media_available():
            raise ValueError("No media is currently available.")
        else:
            return func(self, *args, **kwargs)
    return wrapper


class MediaManager:
    def __init__(self):

        self.youtube_video = youtube_player.YouTubePlayer()
        self.local_video = LocalPlayer(config.LOCAL_VIDEO_PATH)  # Добавлен LocalPlayer

        self.background_video_url = config.BACKGROUND_VIDEO_URL  # Используйте соответствующий модуль для конфигурации
        self.is_background_playing = False

        self.player = vlc.MediaPlayer()
        self.queue = queue.Queue()
        self.thread = threading.Thread(target=self._run)
        self.thread.daemon = True
        self.thread.start()

        self._skip = False

    def _run(self):
        while True:
            url_or_path = self.queue.get()
            try:
                if "youtube.com" in url_or_path or "youtu.be" in url_or_path:
                    video = YouTube(url_or_path)
                    self.youtube_video.set_yt_video_information(video)
                    url = video.streams.get_highest_resolution().url
                else:
                    # Это локальный путь
                    url = url_or_path
                    self.local_video.set_video_information(url)

                media = vlc.Media(url)
                self.player.set_media(media)
                self.player.play()
                self.player.set_fullscreen(True)
                self._skip = False
                while self.player.get_state() != vlc.State.Ended and not self._skip and self.player.get_state() != vlc.State.Stopped:
                    time.sleep(1)

                # Если нужно проиграть фоновое видео после завершения текущего
                if self._skip and self.queue.empty():
                    self._play_background_video()

            except Exception as e:
                print(f"Error playing {url_or_path}: {str(e)}")
            self.queue.task_done()

    # Добавьте методы для фонового видео
    def _play_background_video(self):
        if not self.is_background_playing:
            media = vlc.Media(self.background_video_url)
            self.local_video.set_video_information(self.background_video_url)
            self.player.set_media(media)
            self.player.play()
            self.player.set_fullscreen(True)
            self.is_background_playing = True

    def _stop_background_video(self):
        if self.is_background_playing:
            self.player.stop()
            self.is_background_playing = False


    def play(self, vid):
        self.queue.put(vid)

    def skip(self):
        self._skip = True

    def skip_forward(self, t):
        time_in_secs = self._parse_time_string(t)
        current_time_in_secs = self.player.get_time() // 1000
        new_time_in_secs = current_time_in_secs + time_in_secs
        #time.sleep(0.5)          
        self.player.set_time(new_time_in_secs * 1000)
        #time.sleep(1.5)  

    def skip_backward(self, t):
        time_in_secs = self._parse_time_string(t)
        current_time_in_secs = self.player.get_time() // 1000
        new_time_in_secs = current_time_in_secs - time_in_secs
        if new_time_in_secs < 0:
            new_time_in_secs = 0
        
        #time.sleep(0.5)  # Добавляем задержку в 0.5 секунды
        self.player.set_time(new_time_in_secs * 1000)
        #time.sleep(1.5)  

    def _parse_time_string(self, time_str):
        if ':' in time_str:
            time_parts = time_str.split(':', maxsplit=2)
            if len(time_parts) == 3:
                hours, minutes, seconds = time_parts
                if not hours:
                    hours = 0
                if not minutes:
                    minutes = 0
                if not seconds:
                    seconds = 0
                time_in_secs = int(hours) * 3600 + int(minutes) * 60 + int(seconds)
            elif len(time_parts) == 2:
                minutes, seconds = time_parts
                if not minutes:
                    minutes = 0
                if not seconds:
                    seconds = 0
                time_in_secs = int(minutes) * 60 + int(seconds)
            else:
                time_in_secs = int(time_str)
        else:
            time_in_secs = int(time_str)
        return time_in_secs

    # Сдвиг звука вперед на N секунд
    def volume_shift_forward(self, volume):
        t = self.get_volume() + volume
        self.player.audio_set_volume(t)

    # Сдивг звука назад на N секунд
    def volume_shift_back(self, volume):
        t = self.get_volume() - volume
        self.player.audio_set_volume(t)

    def resume(self):
        self.player.play()

    def pause(self):
        self.player.pause()

    def stop(self):
        self.queue.queue.clear()
        self.player.stop()

    def set_volume(self, volume):
        volume = int(volume)
        self.player.audio_set_volume(volume)

    def get_volume(self):
        return self.player.audio_get_volume()

    def get_time(self):
        position = self.player.get_position()
        if position is None:
            return
        time_in_millis = int(position * self.player.get_length())
        current_time = datetime.timedelta(milliseconds=time_in_millis)
        hours, remainder = divmod(current_time.seconds, 3600)
        minutes, seconds = divmod(remainder, 60)
        return f"{hours:02d}:{minutes:02d}:{seconds:02d}"

    def set_time(self, t):
        new_time_in_secs = self._parse_time_string(t)
        self.player.set_time(new_time_in_secs * 1000)

    def is_playing(self):
        return self.player.get_state() == vlc.State.Playing

    def is_paused(self):
        return self.player.get_state() == vlc.State.Paused

    def is_media_available(self):
        return self.player.get_media() is not None
