import re
import glob
import os

for path in sorted(glob.glob("src/main/resources/config/liquibase/changelog/*_added_entity_*.xml")):
    text = open(path, encoding="utf-8").read()
    for i, block in enumerate(re.findall(r"<createTable[^>]*>.*?</createTable>", text, re.S)):
        cols = re.findall(r'<column name="([^"]+)"', block)
        seen = {}
        dupes = []
        for c in cols:
            seen[c] = seen.get(c, 0) + 1
            if seen[c] == 2:
                dupes.append(c)
        if dupes:
            print(f"{os.path.basename(path)} table#{i}: {dupes}")
