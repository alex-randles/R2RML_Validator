import pandas as pd


def read_data(file_name):
    data = pd.read_csv(file_name)
    return data


def get_row_id(error_message, data):
    for i, row_data in data.iterrows():
        if row_data["RESULT_MESSAGE"] == error_message:
            return i
        print(i, row_data)


def get_script(row_id, error_message, data):
    scripts = {"template IRI doesn't match pattern": maninpulate_iri}
    print(row_id)
    current_value = data["VALUE"][row_id]
    new_value = scripts[error_message](current_value)
    data.at[row_id, 'VALUE'] = new_value
    print(data)


def maninpulate_iri(current_value):
    print(current_value)
    split_value = current_value.split("/")
    print(split_value)
    index_value = "{{{}}}".format(split_value[-1])
    concat = split_value[0:-1] + [index_value]
    new_value = "/".join(concat)
    print(new_value)
    # check value first
    if current_value == " ":
        return new_value
    else:
        return str(current_value) + new_value


if __name__ == "__main__":
    data = read_data('metadata.csv')
    error_message =  "template IRI doesn't match pattern"
    row_id = get_row_id(error_message, data)
    get_script(row_id, error_message, data)
