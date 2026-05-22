from pathlib import Path
root = Path(r"c:/project_workspace/malayalees-us-site-boot")
main = root / "scripts/generate_event_competitions_stack.py"
part2 = root / "scripts/gen_stack_part2.py"
text = main.read_text(encoding="utf-8")
idx = text.find("if __name__")
text = text[:idx]
text += "\n# --- stack generation ---\n"
text += part2.read_text(encoding="utf-8")
text += "\nif __name__ == '__main__':\n    paths = run_stack()\n    print('Generated', len(paths), 'files')\n"
main.write_text(text, encoding="utf-8")
print("merged", main.stat().st_size)
