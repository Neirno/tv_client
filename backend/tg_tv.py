from flask import Flask, request, jsonify
from media_manager import MediaManager
import os

app = Flask(__name__)
media_manager = MediaManager()

os.environ['DISPLAY'] = ':0'

FILMS_ROOT_DIR = '/media/gleb/27841631-aca4-4b5c-b627-768bc8add7c1/torrent/downloads/complete'  # Замените на путь к вашей корневой папке с фильмами
PRIVATE_FILMS_DIR = '/media/gleb/27841631-aca4-4b5c-b627-768bc8add7c1/torrent/downloads/private'  

@app.route('/search_film', methods=['POST'])
def search_film_root():
    categories = [dir_name for dir_name in os.listdir(FILMS_ROOT_DIR) if
                  os.path.isdir(os.path.join(FILMS_ROOT_DIR, dir_name))]
    return jsonify(categories)


@app.route('/search_film/<category>', methods=['POST'])
def search_film_category(category):
    category_path = os.path.join(FILMS_ROOT_DIR, category)
    print(category_path)
    if not os.path.exists(category_path) or not os.path.isdir(category_path):
        return jsonify({"error": "Category not found"}), 404

    films = [file_name for file_name in os.listdir(category_path) if
             os.path.isfile(os.path.join(category_path, file_name))]
    return jsonify(films)


@app.route('/search_film/<category>/<film>', methods=['POST'])
def search_film_info(category, film):
    film_path = os.path.join(FILMS_ROOT_DIR, category, film)
    print(film_path)
    if not os.path.exists(film_path) or not os.path.isfile(film_path):
        return jsonify({"error": "Film not found"}), 404
    media_manager.play(film_path)
    film_info = {
        "category": category,
        "filmName": film,
        "filmSize": os.path.getsize(film_path),
        "filmPath": film_path,
        # Можете добавить другую информацию о фильме, если это необходимо
    }

    return jsonify(film_info)

@app.route('/search_private_film', methods=['POST'])
def search_private_films():
    if not request.json or 'password' not in request.json or request.json['password'] != "nektym13":  # замените на ваш пароль
        return jsonify({"error": "Authentication failed"}), 403

    films = [file_name for file_name in os.listdir(PRIVATE_FILMS_DIR) if
             os.path.isfile(os.path.join(PRIVATE_FILMS_DIR, file_name))]
    return jsonify(films)




# ... ваш предыдущий код ...
@app.route('/play_private_film/<film>', methods=['POST'])
def play_private_film(film):
    film_path = os.path.join(PRIVATE_FILMS_DIR, film)
    if not os.path.exists(film_path) or not os.path.isfile(film_path):
        return jsonify({"error": "Private film not found"}), 404
    media_manager.play(film_path)
    film_info = {
        "category": "private",
        "filmName": film,
        "filmSize": os.path.getsize(film_path),
        "filmPath": film_path,
        # Можете добавить другую информацию о фильме, если это необходимо
    }
    return jsonify(film_info)


from Light import Light

light_controller = Light()

@app.route('/light_on', methods=['POST'])
def light_on():
    try:
        light_controller.light_on()
        return jsonify({"result": "success", "message": "Light turned on."})
    except Exception as e:
        return jsonify({"result": "error", "message": f"Failed to turn on light. Error: {e}"}), 500

@app.route('/light_off', methods=['POST'])
def light_off():
    try:
        light_controller.light_off()
        return jsonify({"result": "success", "message": "Light turned off."})
    except Exception as e:
        return jsonify({"result": "error", "message": f"Failed to turn off light. Error: {e}"}), 500



@app.route('/play', methods=['POST'])
def play_video():
    video_link = request.json.get('url')
    print(f"Playing video: {video_link}")
    media_manager.play(video_link)
    return jsonify({"result": "success", "message": f"Playing video: {video_link}"})


@app.route('/resume', methods=['POST'])
def resume_video():
    print("Resuming video playback")
    media_manager.resume()
    return jsonify({"result": "success", "message": "Resuming video playback"})


@app.route('/pause', methods=['POST'])
def pause_video():
    print("Pausing video playback")
    media_manager.pause()
    return jsonify({"result": "success", "message": "Pausing video playback"})


@app.route('/stop', methods=['POST'])
def stop_video():
    print("Stopping video playback")
    media_manager.stop()
    return jsonify({"result": "success", "message": "Stopping video playback"})


@app.route('/set_volume', methods=['POST'])
def set_volume():
    volume = request.json.get('data')
    print(f"Setting volume: {volume}")
    media_manager.set_volume(volume)
    return jsonify({"result": "success", "message": f"Setting volume: {volume}"})


@app.route('/volume_shift_forward', methods=['POST'])
def volume_shift_forward():
    volume = 10#request.json.get('volume')
    print(f"Shifting volume forward: {volume}")
    media_manager.volume_shift_forward(volume)
    return jsonify({"result": "success", "message": f"Shifting volume forward: {volume}"})


@app.route('/volume_shift_backward', methods=['POST'])
def volume_shift_back():
    volume = 10#request.json.get('volume')
    print(f"Shifting volume back: {volume}")
    media_manager.volume_shift_back(volume)
    return jsonify({"result": "success", "message": f"Shifting volume back: {volume}"})


@app.route('/skip_forward', methods=['POST'])
def skip_forward():
    time_str = "10"#request.json.get('time')
    print(f"Skipping forward: {time_str}")
    media_manager.skip_forward(time_str)
    return jsonify({"result": "success", "message": f"Skipping forward: {time_str}"})


@app.route('/skip_backward', methods=['POST'])
def skip_backward():
    time_str = "10"#request.json.get('time')
    print(f"Skipping backward: {time_str}")
    media_manager.skip_backward(time_str)
    return jsonify({"result": "success", "message": f"Skipping backward: {time_str}"})


@app.route('/set_time', methods=['POST'])
def set_time():
    time_str = request.json.get('data')
    print(f"Setting time: {time_str}")
    media_manager.set_time(time_str)
    return jsonify({"result": "success", "message": f"Setting time: {time_str}"})


@app.route('/display_on', methods=['POST'])
def display_on():
    print(f"display on")
    os.system("xset dpms force on")
    return jsonify({"result": "success", "message": f"Display on"})


@app.route('/display_off', methods=['POST'])
def display_off():
    print(f"display off")
    os.system("xset dpms force off")
    return jsonify({"result": "success", "message": f"Display off"})

from pytubefix import Search
import datetime

@app.route('/search', methods=['POST'])
def search_video():
    data = request.json
    query = data.get("query", "")
    offset = int(data.get("offset", 0))
    limit = int(data.get("limit", 10))

    if not query:
        return jsonify({"error": "Query not provided"}), 400

    yt_search = Search(query)

    videos_to_process = yt_search.results[offset:offset+limit]
    
    videos = []

    # Пропускаем видео, указанные в offset
    #for _ in range(offset):
    #    next(yt_search.results(), None)

    # Собираем следующие 'limit' видео
    for video in videos_to_process:
        video_data = {
            #"title": video.title,
            #"description": video.description,
            #"url": video.watch_url,
            # Добавьте другие необходимые поля
            "title": video.title,
            "channel": video.author,
            "views": video.views,
            "duration": str(datetime.timedelta(seconds=video.length)),
            "thumbnailUrl": video.thumbnail_url,
            "videoUrl": f"https://www.youtube.com/watch?v={video.video_id}"
        }
        videos.append(video_data)

    return jsonify(videos[:limit])


@app.route('/status', methods=['POST'])
def status():
    print("status")
    video_info = media_manager.youtube_video.get_yt_video_information()
    current_time = media_manager.get_time()
    volume = media_manager.get_volume()

    response_data = {
        "result": "success",
        "message": "Video status",
        "title": video_info["title"],
        "channel": video_info["channel"],
        "views": video_info["views"],
        "duration": video_info["duration"],
        "thumbnail_url": video_info["thumbnail_url"],
        "current_time": current_time,
        "volume": volume,
    }
    print(response_data)
    return jsonify(response_data)


@app.route('/skip', methods=['POST'])
def skip():
    print(f"Skip")
    media_manager.skip()
    return jsonify({"result": "success", "message": f"Video skip"})

@app.route('/', methods=['POST'])
def home():
    print(f"home")
    return jsonify({"accept": "ok"}), 200

if __name__ == "__main__":
    os.environ['DISPLAY'] = ':0'
    app.run(host='127.0.0.1', port=12345)

