"""Move preConditions to be the first child of each changeSet (before comment)."""
import re
import glob
import os

CHANGELOG_DIR = "src/main/resources/config/liquibase/changelog"


def fix_changeset(block: str) -> str:
    pre = re.search(
        r"(\s*)<preConditions[^>]*>.*?</preConditions>\s*",
        block,
        re.S,
    )
    if not pre:
        return block
    pre_block = pre.group(0)
    without_pre = block[: pre.start()] + block[pre.end() :]
    # Insert preConditions immediately after changeSet opening tag
    m = re.match(r"(<changeSet[^>]*>\s*)", without_pre, re.S)
    if not m:
        return block
    indent = "        "
    normalized_pre = pre_block.strip()
    if not normalized_pre.startswith("<"):
        return block
    # Re-indent preConditions block
    pre_lines = normalized_pre.splitlines()
    fixed_pre = "\n".join(indent + line.strip() for line in pre_lines if line.strip())
    return without_pre[: m.end()] + fixed_pre + "\n" + without_pre[m.end() :]


def process_file(path: str) -> bool:
    text = open(path, encoding="utf-8").read()
    original = text
    for block in re.findall(r"<changeSet[^>]*>.*?</changeSet>", text, re.S):
        new_block = fix_changeset(block)
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
print("Fixed order:", ", ".join(changed) if changed else "(none)")
