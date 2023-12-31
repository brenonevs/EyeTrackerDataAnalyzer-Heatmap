import pandas as pd
import xml.etree.ElementTree as ET
from xml.dom import minidom
import tkinter as tk
from tkinter import filedialog

def prettify(elem):
    rough_string = ET.tostring(elem, 'utf-8')
    reparsed = minidom.parseString(rough_string)
    return reparsed.toprettyxml(indent="  ")

def read_xml_to_dataframe():
    root = tk.Tk()
    root.withdraw()  
    xml_path = filedialog.askopenfilename(filetypes=[("XML files", "*.xml")])
    if not xml_path: 
        return None, None

    tree = ET.parse(xml_path)
    root = tree.getroot()
    data = [response.attrib for response in root.iter('response')]
    df = pd.DataFrame(data)
    return df, tree

def remove_accidental_target_changes(df):
    current_target = None
    change_indices = []

    for i in range(len(df)):
        if df.iloc[i]['gaze_target'] != current_target:
            if i + 50 < len(df) and df.iloc[i + 50]['gaze_target'] == current_target:
                change_indices.extend(range(i, i + 50))
            else:
                current_target = df.iloc[i]['gaze_target']

    return df.drop(change_indices)

def create_new_xml(df, original_tree, output_filename):
    root = original_tree.getroot()
    root.find('gazes').clear()  

    for _, row in df.iterrows():
        ET.SubElement(root.find('gazes'), 'response', row.to_dict())

    formatted_xml = prettify(root)
    with open(output_filename, 'w', encoding='utf-8') as file:
        file.write(formatted_xml)

df, original_tree = read_xml_to_dataframe()
if df is not None and original_tree is not None:
    df_cleaned = remove_accidental_target_changes(df)
    create_new_xml(df_cleaned, original_tree, 'output_cleaned.xml')
