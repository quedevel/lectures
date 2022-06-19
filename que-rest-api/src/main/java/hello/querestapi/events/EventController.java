package hello.querestapi.events;

import hello.querestapi.index.IndexController;
import hello.querestapi.util.PagedModelUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces = "application/hal+json;charset=UTF-8")
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Validated EventDto eventDto, Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body(EntityModel.of(errors, linkTo(methodOn(IndexController.class).index()).withRel("index")));
        }
        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body(EntityModel.of(errors, linkTo(methodOn(IndexController.class).index()).withRel("index")));
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        eventRepository.save(event);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(event.getId());
        URI createdUri = selfLinkBuilder.toUri();

        EntityModel<Event> entityModel = EntityModel.of(event,
                linkTo(EventController.class).withRel("query-events"),
                selfLinkBuilder.withSelfRel(),
                selfLinkBuilder.withRel("update-event"),
                Link.of("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(entityModel);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler){
        Page<Event> events = eventRepository.findAll(pageable);

        PagedModel<EntityModel<Event>> pagedModel =
                PagedModelUtil.getEntityModels(assembler, events, EventController.class, Event::getId);

        pagedModel.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id){
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            Event event = optionalEvent.get();
            return ResponseEntity.ok(
                    EntityModel.of(event,
                    linkTo(EventController.class).slash(event.getId()).withSelfRel(),
                    Link.of("/docs/index.html#resources-events-get").withRel("profile"))
            );
        }
    }

}
