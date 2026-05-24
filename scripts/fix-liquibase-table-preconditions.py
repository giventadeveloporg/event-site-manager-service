"""Add tableExists preConditions to Liquibase changesets that alter tables without checking table existence."""
import re
import glob
import os

CHANGELOG_DIR = "src/main/resources/config/liquibase/changelog"

# Operations that need tableExists when tableName is known
TABLE_OPS = re.compile(
    r"<(addColumn|modifyDataType|addNotNullConstraint|dropColumn|createIndex|addUniqueConstraint|addForeignKeyConstraint)\s+[^>]*tableName=\"([^\"]+)\""
)


def extract_table_from_changeset(block: str) -> str | None:
    m = TABLE_OPS.search(block)
    if m:
        return m.group(2)
    # sql UPDATE table_name
    m2 = re.search(r"\bUPDATE\s+([a-z_][a-z0-9_]*)\s+SET\b", block, re.I)
    if m2:
        return m2.group(1)
    return None


def has_table_exists(block: str, table: str) -> bool:
    return f'tableName="{table}"' in block and "<tableExists" in block


def inject_table_exists(block: str, table: str) -> str:
    if has_table_exists(block, table):
        return block
    pre = re.search(r"<preConditions([^>]*)>", block)
    if pre:
        insert = f'            <tableExists tableName="{table}"/>\n'
        return block.replace(
            pre.group(0),
            pre.group(0) + "\n" + insert,
            1,
        )
    # Insert preConditions before first structural change
    for tag in (
        "addColumn",
        "modifyDataType",
        "addNotNullConstraint",
        "createIndex",
        "sql",
        "dropColumn",
    ):
        idx = block.find(f"<{tag}")
        if idx != -1:
            pre_block = (
                f'        <preConditions onFail="MARK_RAN">\n'
                f'            <tableExists tableName="{table}"/>\n'
                f"        </preConditions>\n"
            )
            return block[:idx] + pre_block + block[idx:]
    return block


def process_file(path: str) -> bool:
    text = open(path, encoding="utf-8").read()
    original = text
    for block in re.findall(r"<changeSet[^>]*>.*?</changeSet>", text, re.S):
        table = extract_table_from_changeset(block)
        if not table:
            continue
        new_block = inject_table_exists(block, table)
        if new_block != block:
            text = text.replace(block, new_block, 1)
    if text != original:
        open(path, "w", encoding="utf-8").write(text)
        return True
    return False


changed = []
for path in sorted(glob.glob(os.path.join(CHANGELOG_DIR, "*.xml"))):
    if process_file(path):
        changed.append(os.path.basename(path))

print("Updated:", ", ".join(changed) if changed else "(none)")
