# imported modules
from flask import Flask, render_template, request, send_file, send_from_directory
from werkzeug import secure_filename
from generate_report import run_validation
from query_result import *
import os
import subprocess
import rdflib

app = Flask(__name__)
UPLOAD_FOLDER = 'static/uploads'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
# to prevent caching of previous result
file_counter = 0
app.config["allowed_file_extensions"] = ["ttl"]
app.config['SEND_FILE_MAX_AGE_DEFAULT'] = 0
file_name = ""


class API:

    def __init__(self):
        app.run(host="127.0.0.1", port="5000", threaded=True, debug=True)

    @app.route("/", methods=["GET", "POST"])
    def get_files():
            if request.method == "POST":
                file = request.files['file_1']
                filename = secure_filename(file.filename)
                file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
                file.save(file_path)
                print(file_path)
                conformity_result = run_validation(file_path, "./resources/function.ttl", "./resources/output.ttl")
                return render_template("result.html", conformity_result=conformity_result)
            else:
                return render_template("index.html")

    @app.after_request
    def add_header(response):
        """
        Add headers to both force latest IE rendering engine or Chrome Frame,
        and also to cache the rendered page for 10 minutes.
        """
        response.headers['X-UA-Compatible'] = 'IE=Edge,chrome=1'
        response.headers['Cache-Control'] = 'public, max-age=0'
        return response


    @app.route("/download_CSV_report/", methods=["GET"])
    def return_report1():
        print("Downloading file.......")
        download_path = "./static/report.csv"
        return send_file(download_path, as_attachment=True, cache_timeout=0)

    @app.route("/download_turtle_report/", methods=["GET"])
    def return_report2():
        print("Downloading file.......")
        download_path = "./resources/output.ttl"
        return send_file(download_path, as_attachment=True, cache_timeout=0)

    @app.route("/download_provenance/", methods=["GET"])
    def return_provenance():
        print("Downloading file.......")
        # create the test properties for R2RML for provenance
        create_test_properties("/home/alex/Desktop/R2RML_Validator/provenance/test.properties", "/home/alex/Desktop/R2RML_Validator/provenance/sample_map.ttl", "/home/alex/Desktop/First_Experiment/R2RML_Validator/static/provenance.ttl", "/home/alex/Desktop/First_Experiment/R2RML_Validator/static/report.csv")
        subprocess.call(["/home/alex/Desktop/R2RML_Validator/provenance/run.sh"])
        download_path = "./static/provenance.ttl"
        return send_file(download_path, as_attachment=True, cache_timeout=0)


if __name__ == "__main__":
    # start api
    API()



