from pathlib import Path
root = Path(r"c:/project_workspace/event-site-manager-service")
main = root / "scripts/generate_event_competitions_stack.py"
fn = (root / "scripts/patch_fn.txt").read_text(encoding="utf-8")
t = main.read_text(encoding="utf-8")
start = t.find("def patch_event_details")
end = t.find("def gen_registration_tests")
main.write_text(t[:start] + fn + t[end:], encoding="utf-8")
print("ok")
