from pathlib import Path
p=Path(r"c:/project_workspace/event-site-manager-service/scripts/generate_event_competitions_stack.py")
t=p.read_text(encoding="utf-8")
start=t.find("def patch_event_details")
end=t.find("def gen_registration_tests")
new = Path(r"c:/project_workspace/event-site-manager-service/scripts/patch_event_details_fix.py").read_text(encoding="utf-8") if Path(r"c:/project_workspace/event-site-manager-service/scripts/patch_event_details_fix.py").exists() else ""
